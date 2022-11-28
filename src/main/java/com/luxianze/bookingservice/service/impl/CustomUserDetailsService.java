package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.entity.User;
import com.luxianze.bookingservice.repository.UserRepository;
import com.luxianze.bookingservice.service.dto.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identity) throws UsernameNotFoundException {

        log.info("Getting user by : {}", identity);

        User user = this.userRepository.findFirstByIdentity(identity);

        if (Objects.isNull(user)) {
            log.error("User with identity : {}, is not found!", identity);
            throw new UsernameNotFoundException("User with identity: " + identity + ", is not found!");
        }

        // TODO: Add authority details

        return new CustomUserDetails(user);
    }
}
