package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.PublicUserDTO;

import java.util.List;
import java.util.Optional;

public interface PublicUserService {
    PublicUserDTO savePublicUser(PublicUserDTO publicUserDTO);
    PublicUserDTO updatePublicUser(PublicUserDTO publicUserDTO);
    Optional<PublicUserDTO> getPublicUserById(Long publicUserId);
    List<PublicUserDTO> getPublicUserListByDeviceId(String deviceId);
}
