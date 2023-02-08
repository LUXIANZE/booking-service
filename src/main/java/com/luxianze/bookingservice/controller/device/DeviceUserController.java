package com.luxianze.bookingservice.controller.device;

import com.luxianze.bookingservice.service.DeviceUserService;
import com.luxianze.bookingservice.service.dto.DeviceUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("device-user")
public class DeviceUserController {

    private final DeviceUserService deviceUserService;

    public DeviceUserController(DeviceUserService deviceUserService) {
        this.deviceUserService = deviceUserService;
    }

    @PostMapping
    public ResponseEntity<DeviceUserDTO> createDeviceUser(@RequestBody DeviceUserDTO deviceUserDTO) throws Exception {

        return ResponseEntity
                .ok(deviceUserService.save(deviceUserDTO));
    }
}
