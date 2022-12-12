package com.luxianze.bookingservice.service.dto;

import lombok.Data;

@Data
public class BookingDTO {
    private Long id;
    private Long userId;
    private Long sessionId;
    private boolean paymentDone;
    private boolean attendance;
}
