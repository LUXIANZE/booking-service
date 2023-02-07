package com.luxianze.bookingservice.controller.payment;

import com.luxianze.bookingservice.entity.Booking;
import com.luxianze.bookingservice.entity.session.details.BadmintonTrainingSessionDetails;
import com.luxianze.bookingservice.repository.BookingRepository;
import com.luxianze.bookingservice.repository.SessionRepository;
import com.luxianze.bookingservice.repository.session.details.BadmintonTrainingSessionDetailsRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("payment")
public class PaymentController {

    @Value("${stripe.key.secret.test}")
    private String secretKey;

    @Value("${stripe.endpoint.secret.test}")
    private String endpointSecret;

    @Value("${booking.service.frontend.base.url}")
    private String bookingServiceFrontendUrl;

    @Value("${booking.service.frontend.pages.payment.success}")
    private String bookingServiceFrontendPaymentSuccessPagePath;

    @Value("${booking.service.frontend.pages.payment.cancel}")
    private String bookingServiceFrontendPaymentCancelPagePath;

    private final BookingRepository bookingRepository;

    private final BadmintonTrainingSessionDetailsRepository badmintonTrainingSessionDetailsRepository;

    private final SessionRepository sessionRepository;

    public PaymentController(BookingRepository bookingRepository, BadmintonTrainingSessionDetailsRepository badmintonTrainingSessionDetailsRepository, SessionRepository sessionRepository) {
        this.bookingRepository = bookingRepository;
        this.badmintonTrainingSessionDetailsRepository = badmintonTrainingSessionDetailsRepository;
        this.sessionRepository = sessionRepository;
    }

    @PostConstruct
    public void setup() {
        Stripe.apiKey = secretKey;
    }

    @PostMapping
    public ResponseEntity<String> PaymentResponseEntity(@RequestBody Long bookingId) throws Exception {

        long quantity = 1L;
        String currency = "myr";
        long amount = 0L;

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new Exception("No Booking with ID : " + bookingId + ". Unable to make payment."));

        com.luxianze.bookingservice.entity.Session session = sessionRepository
                .findById(booking.getSessionId())
                .orElseThrow(() -> new Exception("No Booking with ID : " + booking.getSessionId() + ". Unable to make payment."));

        switch (session.getSessionType()) {
            case BADMINTON_TRAINING -> {
                BadmintonTrainingSessionDetails badmintonTrainingSessionDetails = badmintonTrainingSessionDetailsRepository
                        .findBySessionId(booking.getSessionId())
                        .orElseThrow(() -> new Exception("No Session details with SessionId : " + booking.getSessionId() + ". Unable to make payment."));

                currency = badmintonTrainingSessionDetails.getCurrency();
                amount = badmintonTrainingSessionDetails.getPrice().longValue();
            }
            case HAIR_SALON -> log.info("Hair Salon booking");
            default -> log.warn("Unrecognised session type!");
        }

        log.info("Product: {}", session.getSessionType().name());
        log.info("Quantity: {}", quantity);
        log.info("Amount: {} cents, {}", amount, currency);

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setPaymentMethodOptions(
                                SessionCreateParams
                                        .PaymentMethodOptions
                                        .builder()
                                        .setGrabpay(
                                                SessionCreateParams
                                                        .PaymentMethodOptions
                                                        .Grabpay
                                                        .builder()
                                                        .build()
                                        )
                                        .setFpx(SessionCreateParams
                                                .PaymentMethodOptions
                                                .Fpx
                                                .builder()
                                                .build()
                                        )
                                        .build()
                        )
                        .setSuccessUrl("http://localhost:8080/payment/success")
                        .setCancelUrl("http://localhost:8080/payment/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(quantity)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency(currency)
                                                        .setUnitAmount(amount)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName(session.getSessionType().name().replace("_", ""))
                                                                        .build())
                                                        .build())
                                        .build())
                        // Set booking id for reference, so that can update payment status in client webhook success event
                        .setClientReferenceId(bookingId.toString())
                        .build();

        Session stripeSession = Session.create(params);

        return ResponseEntity
                .ok(stripeSession.getUrl());
    }

    @GetMapping("success")
    public ResponseEntity<String> PaymentSuccess() {
        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .header(HttpHeaders.LOCATION, bookingServiceFrontendUrl + bookingServiceFrontendPaymentSuccessPagePath)
                .build();
    }

    @GetMapping("cancel")
    public ResponseEntity<String> PaymentCancel() {
        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .header(HttpHeaders.LOCATION, bookingServiceFrontendUrl + bookingServiceFrontendPaymentCancelPagePath)
                .build();
    }

    @PostMapping("webhook")
    public ResponseEntity<?> webhook(HttpServletRequest httpServletRequest, @RequestBody String payload) {

        String sigHeader = httpServletRequest.getHeader("Stripe-Signature");
        Event event;

        // Verify Signature
        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
        } catch (SignatureVerificationException e) {
            log.error("Invalid signature exception", e);
            // Invalid signature
            return ResponseEntity
                    .badRequest()
                    .build();
        } catch (Exception e) {
            log.error("Unknown exception", e);
            // Anything else that goes wrong, possibly JSON deserialization error
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
            throw new IllegalStateException(String.format("Unable to deserialize event data object for %s", event));
        }

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded" -> {
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                log.info("Payment for {} succeeded.", paymentIntent.getAmount());
            }
            // Then define and call a method to handle the successful payment intent.
            // handlePaymentIntentSucceeded(paymentIntent);
            case "payment_method.attached" -> {
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                log.info("Payment method: {}", paymentMethod);
            }
            case "checkout.session.completed" -> {
                Session session = (Session) stripeObject;
                log.info("Client reference Id: {}", session.getClientReferenceId());
                log.info("Checkout status: {}", session.getStatus());
                log.info("Checkout payment status: {}", session.getPaymentStatus());
                updateBookingPaymentStatus(Long.valueOf(session.getClientReferenceId()), session.getPaymentStatus());
            }
            // Then define and call a method to handle the successful attachment of a PaymentMethod.
            // handlePaymentMethodAttached(paymentMethod);
            default -> log.info("Unhandled event type: {}", event.getType());
        }
        return ResponseEntity
                .ok()
                .build();
    }

    private void updateBookingPaymentStatus(Long bookingId, String paymentStatus) {
        if ("paid".equals(paymentStatus)) {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow();
            booking.setPaymentDone(true);
            bookingRepository.save(booking);
        }
    }
}
