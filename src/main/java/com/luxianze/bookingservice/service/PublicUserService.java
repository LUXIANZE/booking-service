package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.PublicUserDTO;

import java.util.List;
import java.util.Optional;

public interface PublicUserService {
    PublicUserDTO create(PublicUserDTO publicUserDTO);
    PublicUserDTO update(PublicUserDTO publicUserDTO);
    Optional<PublicUserDTO> getById(Long publicUserId);
    List<PublicUserDTO> getByDeviceId(String deviceId);
}
