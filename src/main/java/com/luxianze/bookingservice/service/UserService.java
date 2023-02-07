package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.SecuredUserDTO;
import com.luxianze.bookingservice.service.dto.UserDTO;

import java.util.List;

public interface UserService {
    SecuredUserDTO registerUser(UserDTO userDTO) throws Exception;
    SecuredUserDTO create(UserDTO userDTO) throws Exception;
    List<SecuredUserDTO> findAll();
    SecuredUserDTO findPublicInfoByIdentity(String identity) throws Exception;

    /**
     * Find by Identity while exposing pin(encrypted) field
     * @param identity identity of user
     * @return user matching the provided identity
     */
    UserDTO insecureFindByIdentity(String identity) throws Exception;
}
