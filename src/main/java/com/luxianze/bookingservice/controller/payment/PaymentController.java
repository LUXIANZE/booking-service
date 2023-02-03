package com.luxianze.bookingservice.controller.payment;

import com.luxianze.bookingservice.constant.entity.SessionType;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("payment")
public class PaymentController {

    @Value("${stripe.key.secret.test}")
    private String secretKey;

    @Value("${booking.service.frontend.base.url}")
    private String bookingServiceFrontendUrl;

    @Value("${booking.service.frontend.pages.payment.success}")
    private String bookingServiceFrontendPaymentSuccessPagePath;

    @Value("${booking.service.frontend.pages.payment.cancel}")
    private String bookingServiceFrontendPaymentCancelPagePath;

    public PaymentController() {
    }

    @PostMapping
    public ResponseEntity<String> PaymentResponseEntity() throws StripeException {
        Stripe.apiKey = secretKey;

        /*
          TODO:
          1. Collect customer details
          2. Create check out session with useful info
          3. Create Booking upon check out, and update status to paid upon payment success webhook
         */
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
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("myr")
                                                        .setUnitAmount(10000L)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName(SessionType.BADMINTON_TRAINING.name())
                                                                        .build())
                                                        .build())
                                        .build())
                        .build();

        Session session = Session.create(params);

        return ResponseEntity
                .ok(session.getUrl());
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

}
