package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.PublicUserInfoDTO;
import com.luxianze.bookingservice.service.dto.UserDTO;

import java.util.List;

public interface UserService {
    PublicUserInfoDTO registerUser(UserDTO userDTO) throws Exception;
    List<PublicUserInfoDTO> findAll();

    UserDTO findByIdentity(String identity);
    PublicUserInfoDTO findPublicInfoByIdentity(String identity);
}
