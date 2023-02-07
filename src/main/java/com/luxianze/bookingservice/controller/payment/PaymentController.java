package com.luxianze.bookingservice.controller.payment;

import com.luxianze.bookingservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("payment")
public class PaymentController {

    @Value("${booking.service.frontend.base.url}")
    private String bookingServiceFrontendUrl;

    @Value("${booking.service.frontend.pages.payment.success}")
    private String bookingServiceFrontendPaymentSuccessPagePath;

    @Value("${booking.service.frontend.pages.payment.cancel}")
    private String bookingServiceFrontendPaymentCancelPagePath;

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> PaymentResponseEntity(@RequestBody Long bookingId) throws Exception {

        String paymentLink = paymentService.payment(bookingId);

        return ResponseEntity
                .ok(paymentLink);
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

        try {
            paymentService.eventHandler(sigHeader, payload);

            return ResponseEntity
                    .ok()
                    .build();
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }
}
