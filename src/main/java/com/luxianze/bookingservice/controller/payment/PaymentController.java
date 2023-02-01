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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("payment")
public class PaymentController {

    @Value("${stripe.key.secret.test}")
    private String secretKey;

    public PaymentController() {
    }

    @PostMapping("response-entity")
    public ResponseEntity<?> PaymentResponseEntity() throws StripeException {
        Stripe.apiKey = secretKey;

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
                .status(HttpStatus.PERMANENT_REDIRECT)
                .header(HttpHeaders.LOCATION, session.getUrl())
                .build();
    }

    @GetMapping("success")
    public ResponseEntity<String> PaymentSuccess() {
        return ResponseEntity.ok("Payment Successfully Made!");
    }

    @GetMapping("cancel")
    public ResponseEntity<String> PaymentCancel() {
        return ResponseEntity.ok("Request to Cancel Payment!");
    }
}
