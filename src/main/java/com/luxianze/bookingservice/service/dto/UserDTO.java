package com.luxianze.bookingservice.service.dto;

import com.luxianze.bookingservice.constant.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String identity;
    private String phoneNumber;
    private String pin;
    private Role role;
}
