package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.constant.entity.Role;
import com.luxianze.bookingservice.entity.User;
import com.luxianze.bookingservice.repository.UserRepository;
import com.luxianze.bookingservice.service.UserService;
import com.luxianze.bookingservice.service.dto.SecuredUserDTO;
import com.luxianze.bookingservice.service.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    public SecuredUserDTO registerUser(UserDTO userDTO) throws Exception {
        /*
        Disregard the id field while registering user
         */

        validateRegisteringDetails(userDTO);
        validateEmail(userDTO);

        User user = new User();
        user.setIdentity(userDTO.getIdentity());
        user.setPin(passwordEncoder.encode(userDTO.getPin()));
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setRole(Role.PUBLIC); // Assign default role as public

        User createdUser = this.userRepository.save(user);

        return mapUserToPublicUserInfoDTO(createdUser);
    }

    private void validateEmail(UserDTO userDTO) throws Exception {

        if (Objects.nonNull(userDTO.getEmail())) {
            Pattern VALID_EMAIL_ADDRESS_REGEX =
                    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(userDTO.getEmail());

            if (!matcher.find()) {
                throw new Exception("Invalid email pattern, please make sure email is following xxx@xxx.xxx pattern");
            }
        }
    }

    /**
     * Check whether are the details already in use
     *
     * @param userDTO user
     * @throws Exception thrown whenever identity or phone number is taken
     */
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
    public List<SecuredUserDTO> findAll() {
        return this.userRepository.findAll().stream().map(this::mapUserToPublicUserInfoDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO insecureFindByIdentity(String identity) throws Exception {
        User user = findUserByIdentity(identity);
        return mapUserToUserDTO(user);
    }

    @Override
    public SecuredUserDTO findPublicInfoByIdentity(String identity) throws Exception {
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

    private SecuredUserDTO mapUserToPublicUserInfoDTO(User user) {

        SecuredUserDTO securedUserDTO = new SecuredUserDTO();
        securedUserDTO.setId(user.getId());
        securedUserDTO.setRole(user.getRole());
        securedUserDTO.setIdentity(user.getIdentity());
        securedUserDTO.setPhoneNumber(user.getPhoneNumber());
        securedUserDTO.setEmail(user.getEmail());

        return securedUserDTO;
    }


    private UserDTO mapUserToUserDTO(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setRole(user.getRole());
        userDTO.setIdentity(user.getIdentity());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setEmail(user.getEmail());
        userDTO.setPin(user.getPin());

        return userDTO;
    }
}
