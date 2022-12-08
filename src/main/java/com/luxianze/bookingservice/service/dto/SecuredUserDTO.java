package com.luxianze.bookingservice.service.dto;

import com.luxianze.bookingservice.constant.entity.Role;
import lombok.Data;

/**
 * This DTO excludes sensitive fields such as pin from the public
 */
@Data
public class SecuredUserDTO {
    private Long id;
    private String identity;
    private String phoneNumber;
    private String email;
    private Role role;
}
