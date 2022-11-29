package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.PublicUserInfoDTO;
import com.luxianze.bookingservice.service.dto.UserDTO;

import java.util.List;

public interface UserService {
    PublicUserInfoDTO registerUser(UserDTO userDTO) throws Exception;
    List<PublicUserInfoDTO> findAll();

    /**
     * Find by Identity while exposing pin(encrypted) field
     * @param identity identity of user
     * @return user matching the provided identity
     */
    UserDTO insecureFindByIdentity(String identity);
    PublicUserInfoDTO findPublicInfoByIdentity(String identity);
}
