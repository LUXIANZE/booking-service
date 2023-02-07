package com.luxianze.bookingservice.service;

public interface PaymentService {
    String payment(Long bookingId) throws Exception;
    void eventHandler(String stripeSignatureHeader, String payload) throws Exception;
}
