package com.luxianze.bookingservice.service.dto;

import com.luxianze.bookingservice.constant.entity.SessionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionDTO {
    private Long id;
    private LocalDateTime dateTime;
    private int totalSlots;
    private SessionType sessionType;
}
