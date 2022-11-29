package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.constant.entity.Role;
import com.luxianze.bookingservice.entity.User;
import com.luxianze.bookingservice.repository.UserRepository;
import com.luxianze.bookingservice.service.UserService;
import com.luxianze.bookingservice.service.dto.PublicUserInfoDTO;
import com.luxianze.bookingservice.service.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PublicUserInfoDTO registerUser(UserDTO userDTO) throws Exception {
        /*
        Disregard the id field while registering user
         */

        validateRegisteringDetails(userDTO);

        User user = new User();
        user.setIdentity(userDTO.getIdentity());
        user.setPin(passwordEncoder.encode(userDTO.getPin()));
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(Role.PUBLIC); // Assign default role as public

        User createdUser = this.userRepository.save(user);

        return mapUserToPublicUserInfoDTO(createdUser);
    }

    private void validateRegisteringDetails(UserDTO userDTO) throws Exception {
        boolean existsByIdentity = this.userRepository.existsByIdentity(userDTO.getIdentity());
        if (existsByIdentity) {
            throw new Exception("Identity :" + userDTO.getIdentity() + ", is already registered.");
        }

        boolean existsByPhoneNumber = this.userRepository.existsByPhoneNumber(userDTO.getPhoneNumber());
        if (existsByPhoneNumber) {
            throw new Exception("Phone Number :" + userDTO.getPhoneNumber() + ", is already taken by another user.");
        }
    }

    @Override
    public List<PublicUserInfoDTO> findAll() {
        return this.userRepository.findAll().stream().map(this::mapUserToPublicUserInfoDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO insecureFindByIdentity(String identity) throws Exception {
        User user = findUserByIdentity(identity);
        return mapUserToUserDTO(user);
    }

    @Override
    public PublicUserInfoDTO findPublicInfoByIdentity(String identity) throws Exception {
        User user = findUserByIdentity(identity);
        return mapUserToPublicUserInfoDTO(user);
    }

    private User findUserByIdentity(String identity) throws Exception {
        Optional<User> optionalUser = this.userRepository.findOneByIdentity(identity);
        if (optionalUser.isEmpty()) {
            throw new Exception("User with identity: " + identity + ", not found");
        }
        return optionalUser.get();
    }

    private PublicUserInfoDTO mapUserToPublicUserInfoDTO(User user) {

        PublicUserInfoDTO publicUserInfoDTO = new PublicUserInfoDTO();
        publicUserInfoDTO.setId(user.getId());
        publicUserInfoDTO.setRole(user.getRole());
        publicUserInfoDTO.setIdentity(user.getIdentity());
        publicUserInfoDTO.setPhoneNumber(user.getPhoneNumber());

        return publicUserInfoDTO;
    }


    private UserDTO mapUserToUserDTO(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setRole(user.getRole());
        userDTO.setIdentity(user.getIdentity());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setPin(user.getPin());

        return userDTO;
    }
}
