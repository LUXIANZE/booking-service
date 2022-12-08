package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.SecuredUserDTO;
import com.luxianze.bookingservice.service.dto.UserDTO;

import java.util.List;

public interface UserService {
    // FIXME: dont throw exception, think about idempotency and use optional
    SecuredUserDTO registerUser(UserDTO userDTO) throws Exception;
    List<SecuredUserDTO> findAll();

    /**
     * Find by Identity while exposing pin(encrypted) field
     * @param identity identity of user
     * @return user matching the provided identity
     */
    UserDTO insecureFindByIdentity(String identity) throws Exception;
    SecuredUserDTO findPublicInfoByIdentity(String identity) throws Exception;
}
