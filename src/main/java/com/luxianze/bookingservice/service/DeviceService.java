package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.DeviceDTO;

import java.util.Optional;

public interface DeviceService {
    DeviceDTO save(DeviceDTO deviceDTO);
    Optional<DeviceDTO> getById(String deviceId);
}
