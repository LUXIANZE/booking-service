package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.DevicePublicUserDTO;

import java.util.List;
import java.util.Optional;

public interface DevicePublicUserService {
    DevicePublicUserDTO save(DevicePublicUserDTO devicePublicUserDTO) throws Exception;
    List<DevicePublicUserDTO> getByDeviceId(String deviceId);
    Optional<DevicePublicUserDTO> get(DevicePublicUserDTO devicePublicUserDTO);
}
