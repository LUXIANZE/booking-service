package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.entity.Booking;
import com.luxianze.bookingservice.entity.session.details.BadmintonTrainingSessionDetails;
import com.luxianze.bookingservice.repository.BookingRepository;
import com.luxianze.bookingservice.repository.SessionRepository;
import com.luxianze.bookingservice.repository.session.details.BadmintonTrainingSessionDetailsRepository;
import com.luxianze.bookingservice.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.key.secret.test}")
    private String secretKey;
    @Value("${stripe.endpoint.secret.test}")
    private String endpointSecret;
    @Value("${booking.service.backend.base.url}")
    private String bookingServiceBackendBaseUrl;
    @Value("${booking.service.backend.endpoints.payment.success}")
    private String bookingServiceBackendEndpointsPaymentSuccess;
    @Value("${booking.service.backend.endpoints.payment.cancel}")
    private String bookingServiceBackendEndpointsPaymentCancel;

    private final BookingRepository bookingRepository;
    private final SessionRepository sessionRepository;
    private final BadmintonTrainingSessionDetailsRepository badmintonTrainingSessionDetailsRepository;

    public PaymentServiceImpl(BookingRepository bookingRepository, SessionRepository sessionRepository, BadmintonTrainingSessionDetailsRepository badmintonTrainingSessionDetailsRepository) {
        this.bookingRepository = bookingRepository;
        this.sessionRepository = sessionRepository;
        this.badmintonTrainingSessionDetailsRepository = badmintonTrainingSessionDetailsRepository;
    }

    @PostConstruct
    public void setup() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public String payment(Long bookingId) throws Exception {
        long quantity = 1L;
        String currency = "myr";
        long amount = 0L;

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new Exception("No Booking with ID : " + bookingId + ". Unable to make payment."));

        com.luxianze.bookingservice.entity.Session session = sessionRepository
                .findById(booking.getSessionId())
                .orElseThrow(() -> new Exception("No Booking with SessionId : " + booking.getSessionId() + ". Unable to make payment."));

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
                        .setSuccessUrl(bookingServiceBackendBaseUrl + bookingServiceBackendEndpointsPaymentSuccess)
                        .setCancelUrl(bookingServiceBackendBaseUrl + bookingServiceBackendEndpointsPaymentCancel)
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

        return stripeSession.getUrl();
    }

    @Override
    public void eventHandler(String stripeSignatureHeader, String payload) throws Exception {
        Event event;

        // Verify Signature
        try {
            event = Webhook.constructEvent(
                    payload, stripeSignatureHeader, endpointSecret
            );
        } catch (SignatureVerificationException e) {
            log.error("Invalid signature exception", e);
            // Invalid signature
            throw e;
        } catch (Exception e) {
            log.error("Unknown exception", e);
            // Anything else that goes wrong, possibly JSON deserialization error
            throw e;
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
    }

    private void updateBookingPaymentStatus(Long bookingId, String paymentStatus) {
        if ("paid".equals(paymentStatus)) {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow();
            booking.setPaymentDone(true);
            bookingRepository.save(booking);
        }
    }
}
