package com.luxianze.bookingservice.controller.user;

import com.luxianze.bookingservice.service.UserService;
import com.luxianze.bookingservice.service.dto.SecuredUserDTO;
import com.luxianze.bookingservice.service.dto.UserDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @SecurityRequirement(name = "bearer-key")
    @PreAuthorize("hasAnyAuthority({'SCOPE_SUPERUSER','SCOPE_ADMIN', 'SCOPE_COACH', 'SCOPE_TEACHER'})")
    public ResponseEntity<List<SecuredUserDTO>> getUser() {
        // TODO: Refactor to allow pagination
        return ResponseEntity.ok(this.userService.findAll());
    }

    @GetMapping("/{identity}")
    @SecurityRequirement(name = "bearer-key")
    @PreAuthorize("hasAnyAuthority({'SCOPE_SUPERUSER','SCOPE_ADMIN', 'SCOPE_COACH', 'SCOPE_TEACHER'})")
    public ResponseEntity<SecuredUserDTO> getUserByIdentity(@PathVariable String identity) {
        try {
            // TODO: Refactor to use optional
            return ResponseEntity.ok(this.userService.findPublicInfoByIdentity(identity));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // TODO: Update to use Page, and migrate to --> device/:deviceId/user
    @GetMapping("/deviceId/{deviceId}")
    public ResponseEntity<List<SecuredUserDTO>> getUserByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity
                .ok(userService.findUsersByDeviceId(deviceId));
    }

    @PostMapping
    public ResponseEntity<SecuredUserDTO> create(@RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity
                .ok(userService.create(userDTO));
    }

}
