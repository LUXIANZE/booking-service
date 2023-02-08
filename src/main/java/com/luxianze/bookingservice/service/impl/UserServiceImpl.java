package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.constant.entity.Role;
import com.luxianze.bookingservice.entity.User;
import com.luxianze.bookingservice.repository.DeviceUserRepository;
import com.luxianze.bookingservice.repository.UserRepository;
import com.luxianze.bookingservice.service.UserService;
import com.luxianze.bookingservice.service.dto.SecuredUserDTO;
import com.luxianze.bookingservice.service.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeviceUserRepository deviceUserRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, DeviceUserRepository deviceUserRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.deviceUserRepository = deviceUserRepository;
    }

    @Override
    public SecuredUserDTO registerUser(UserDTO userDTO) throws Exception {

        User user;

        if (Objects.isNull(userDTO.getId())) {
            UserDTO createdUser = rawCreate(userDTO);
            user = mapUserDTO_ToUser(createdUser);
        } else {
            user = this.userRepository
                    .findById(userDTO.getId())
                    .orElseThrow(() -> new NoSuchElementException("No User with ID: " + userDTO.getId() + " found."));
        }

        user.setPin(passwordEncoder.encode(userDTO.getPin()));

        User registeredUser = this.userRepository.save(user);

        return mapUserToSecuredUserDTO(registeredUser);
    }

    @Override
    public SecuredUserDTO create(UserDTO userDTO) throws Exception {
        UserDTO createdUser = rawCreate(userDTO);
        return mapUserDTOToSecuredUserDTO(createdUser);
    }

    public UserDTO rawCreate(UserDTO userDTO) throws Exception {

        validateRegisteringDetails(userDTO);
        validateEmail(userDTO);

        User user = new User();
        user.setIdentity(userDTO.getIdentity());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(Role.PUBLIC); // user role must be defaulted to PUBLIC, shall be updated later by user with SUPERUSER/ADMIN role

        return mapUserToUserDTO(this.userRepository.save(user));
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

        boolean existsByEmail = this.userRepository.existsByEmail(userDTO.getEmail());
        if (existsByEmail) {
            throw new Exception("Email :" + userDTO.getEmail() + ", is already taken by another user.");
        }
    }

    @Override
    public List<SecuredUserDTO> findAll() {
        return this.userRepository.findAll().stream().map(this::mapUserToSecuredUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO insecureFindByIdentity(String identity) throws Exception {
        User user = findUserByIdentity(identity);
        return mapUserToUserDTO(user);
    }

    @Override
    public SecuredUserDTO findPublicInfoByIdentity(String identity) throws Exception {
        User user = findUserByIdentity(identity);
        return mapUserToSecuredUserDTO(user);
    }

    @Override
    public List<SecuredUserDTO> findUsersByDeviceId(String deviceId) {
        return deviceUserRepository
                .findAllByDeviceId(deviceId)
                .parallelStream()
                .map(deviceUser -> userRepository.findById(deviceUser.getUserId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::mapUserToSecuredUserDTO)
                .collect(Collectors.toList());
    }

    private User findUserByIdentity(String identity) throws Exception {
        Optional<User> optionalUser = this.userRepository.findOneByIdentity(identity);
        if (optionalUser.isEmpty()) {
            throw new Exception("User with identity: " + identity + ", not found");
        }
        return optionalUser.get();
    }

    private SecuredUserDTO mapUserToSecuredUserDTO(User user) {

        SecuredUserDTO securedUserDTO = new SecuredUserDTO();
        securedUserDTO.setId(user.getId());
        securedUserDTO.setRole(user.getRole());
        securedUserDTO.setIdentity(user.getIdentity());
        securedUserDTO.setPhoneNumber(user.getPhoneNumber());
        securedUserDTO.setEmail(user.getEmail());

        return securedUserDTO;
    }

    private SecuredUserDTO mapUserDTOToSecuredUserDTO(UserDTO userDTO) {

        SecuredUserDTO securedUserDTO = new SecuredUserDTO();
        securedUserDTO.setRole(userDTO.getRole());
        securedUserDTO.setId(userDTO.getId());
        securedUserDTO.setIdentity(userDTO.getIdentity());
        securedUserDTO.setPhoneNumber(userDTO.getPhoneNumber());
        securedUserDTO.setEmail(userDTO.getEmail());

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

    private User mapUserDTO_ToUser(UserDTO userDTO) {

        User user = new User();
        user.setRole(userDTO.getRole());
        user.setId(userDTO.getId());
        user.setIdentity(userDTO.getIdentity());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setPin(userDTO.getPin());

        return user;
    }
}
