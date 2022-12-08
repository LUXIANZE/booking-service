package com.luxianze.bookingservice.controller.user;

import com.luxianze.bookingservice.service.PublicUserService;
import com.luxianze.bookingservice.service.dto.PublicUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("public-user")
public class PublicUserController {

    private final PublicUserService publicUserService;

    public PublicUserController(PublicUserService publicUserService) {
        this.publicUserService = publicUserService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicUserDTO> getPublicUserById(@PathVariable Long id) {

        return publicUserService.getPublicUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PublicUserDTO> createPublicUser(@RequestBody PublicUserDTO publicUserDTO) {

        return ResponseEntity
                .ok(publicUserService.savePublicUser(publicUserDTO));
    }

    @PutMapping
    public ResponseEntity<PublicUserDTO> updatePublicUser(@RequestBody PublicUserDTO publicUserDTO) {

        return ResponseEntity
                .ok(publicUserService.updatePublicUser(publicUserDTO));
    }

    @GetMapping("/get-by-device-id/{deviceId}")
    public ResponseEntity<List<PublicUserDTO>> getPublicUserListByDeviceId(@PathVariable String deviceId) {

        return ResponseEntity
                .ok(publicUserService.getPublicUserListByDeviceId(deviceId));
    }
}
