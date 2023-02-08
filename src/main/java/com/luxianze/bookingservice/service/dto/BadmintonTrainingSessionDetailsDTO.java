package com.luxianze.bookingservice.service.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class BadmintonTrainingSessionDetailsDTO {
    private Long id;
    private Long sessionId;
    private double durationInHours;
    private BigInteger price;
    private String currency;
    private Long coachId;
}
