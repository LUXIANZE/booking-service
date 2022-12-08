package com.luxianze.bookingservice.controller.user;

import com.luxianze.bookingservice.service.UserService;
import com.luxianze.bookingservice.service.dto.SecuredUserDTO;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<SecuredUserDTO>> getUser() {
        return ResponseEntity.ok(this.userService.findAll());
    }

    @GetMapping("/{identity}")
    public ResponseEntity<SecuredUserDTO> getUserByIdentity(@PathVariable String identity) {
        try {
            return ResponseEntity.ok(this.userService.findPublicInfoByIdentity(identity));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/secured")
    @PreAuthorize("hasAuthority('SCOPE_SUPERUSER')")
    public String securedStuff() {
        return "secured stuff";
    }

}
