package com.luxianze.bookingservice.controller.user;

import com.luxianze.bookingservice.service.UserService;
import com.luxianze.bookingservice.service.dto.PublicUserInfoDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<PublicUserInfoDTO> getUser() {
        return this.userService.findAll();
    }

    @GetMapping("/{identity}")
    public PublicUserInfoDTO getUserByIdentity(@PathVariable String identity) throws Exception {
        return this.userService.findPublicInfoByIdentity(identity);
    }

    @GetMapping("/secured")
    @PreAuthorize("hasAuthority('SCOPE_SUPERUSER')")
    public String securedStuff() {
        return "secured stuff";
    }

}
