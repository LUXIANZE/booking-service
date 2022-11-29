package com.luxianze.bookingservice.controller.security;

import java.time.Instant;

import com.luxianze.bookingservice.service.UserService;
import com.luxianze.bookingservice.service.dto.LoginDTO;
import com.luxianze.bookingservice.service.dto.PublicUserInfoDTO;
import com.luxianze.bookingservice.service.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
public class SecurityController {

    private final JwtEncoder encoder;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SecurityController(JwtEncoder encoder, UserService userService, PasswordEncoder passwordEncoder) {
        this.encoder = encoder;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String token(@RequestBody LoginDTO loginDTO) throws Exception {
        Instant now = Instant.now();
        long expiry = 36000L;

        UserDTO userDTO = this.userService.insecureFindByIdentity(loginDTO.getIdentity());

        boolean isPinMatches = passwordEncoder.matches(loginDTO.getPin(), userDTO.getPin());

        if (!isPinMatches) {
            throw new Exception("Invalid Pin for user: " + loginDTO.getIdentity());
        }

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(loginDTO.getIdentity())
                .claim("scope", userDTO.getRole())
                .build();
        // @formatter:on

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @PostMapping("/register")
    public PublicUserInfoDTO registerUser(@RequestBody UserDTO userDTO) throws Exception {
        return this.userService.registerUser(userDTO);
    }

    @GetMapping
    public String test(){
        return "{ 'status':'valid' }";
    }
}
