package com.luxianze.bookingservice.controller.device;

import com.luxianze.bookingservice.service.DevicePublicUserService;
import com.luxianze.bookingservice.service.dto.DevicePublicUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("device-public-user")
public class DevicePublicUserController {

    private final DevicePublicUserService devicePublicUserService;

    public DevicePublicUserController(DevicePublicUserService devicePublicUserService) {
        this.devicePublicUserService = devicePublicUserService;
    }

    @PostMapping
    public ResponseEntity<DevicePublicUserDTO> createDevicePublicUser(@RequestBody DevicePublicUserDTO devicePublicUserDTO) throws Exception {

        return ResponseEntity
                .ok(devicePublicUserService.save(devicePublicUserDTO));
    }
}
