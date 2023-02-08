package com.luxianze.bookingservice.service;

import com.luxianze.bookingservice.service.dto.DeviceUserDTO;

import java.util.List;
import java.util.Optional;

public interface DeviceUserService {
    DeviceUserDTO save(DeviceUserDTO deviceUserDTO) throws Exception;
    List<DeviceUserDTO> getByDeviceId(String deviceId);
    Optional<DeviceUserDTO> get(Long deviceUserId);
}
