package com.luxianze.bookingservice.controller.device;

import com.luxianze.bookingservice.service.DeviceService;
import com.luxianze.bookingservice.service.dto.DeviceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("device")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceDTO> getDeviceById(@PathVariable String deviceId) {
        return deviceService
                .getById(deviceId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO deviceDTO) {
        return ResponseEntity.ok(deviceService.save(deviceDTO));
    }
}
