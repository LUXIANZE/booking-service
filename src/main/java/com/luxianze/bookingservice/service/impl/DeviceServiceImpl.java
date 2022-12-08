package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.entity.Device;
import com.luxianze.bookingservice.repository.DeviceRepository;
import com.luxianze.bookingservice.service.DeviceService;
import com.luxianze.bookingservice.service.dto.DeviceDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public DeviceDTO save(DeviceDTO deviceDTO) {

        Device device = mapDeviceDTO_ToDevice(deviceDTO);
        Device savedDevice = deviceRepository.save(device);

        return mapDeviceToDeviceDTO(savedDevice);
    }

    @Override
    public Optional<DeviceDTO> getById(String deviceId) {
        return deviceRepository
                .findById(deviceId)
                .map(this::mapDeviceToDeviceDTO);
    }

    private DeviceDTO mapDeviceToDeviceDTO(Device device) {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(device.getId());
        return deviceDTO;
    }

    private Device mapDeviceDTO_ToDevice(DeviceDTO deviceDTO) {
        Device device = new Device();
        device.setId(deviceDTO.getDeviceId());
        return device;
    }
}
