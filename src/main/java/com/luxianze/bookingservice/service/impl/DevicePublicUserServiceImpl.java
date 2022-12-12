package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.entity.DevicePublicUser;
import com.luxianze.bookingservice.entity.composite.keys.DevicePublicUserId;
import com.luxianze.bookingservice.repository.DevicePublicUserRepository;
import com.luxianze.bookingservice.repository.DeviceRepository;
import com.luxianze.bookingservice.repository.PublicUserRepository;
import com.luxianze.bookingservice.service.DevicePublicUserService;
import com.luxianze.bookingservice.service.dto.DevicePublicUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class DevicePublicUserServiceImpl implements DevicePublicUserService {

    private final DevicePublicUserRepository devicePublicUserRepository;
    private final DeviceRepository deviceRepository;
    private final PublicUserRepository publicUserRepository;

    public DevicePublicUserServiceImpl(DevicePublicUserRepository devicePublicUserRepository, DeviceRepository deviceRepository, PublicUserRepository publicUserRepository) {
        this.devicePublicUserRepository = devicePublicUserRepository;
        this.deviceRepository = deviceRepository;
        this.publicUserRepository = publicUserRepository;
    }

    @Override
    public DevicePublicUserDTO save(DevicePublicUserDTO devicePublicUserDTO) throws Exception {
        boolean isDevicePublicUserValid = validateDevicePublicUser(devicePublicUserDTO);

        if (!isDevicePublicUserValid) {
            throw new Exception("Invalid DevicePublicUser");
        }

        DevicePublicUser devicePublicUser = mapDevicePublicUserDTO_ToDevicePublicUser(devicePublicUserDTO);
        DevicePublicUser savedDevicePublicUser = devicePublicUserRepository.save(devicePublicUser);

        return mapDevicePublicUserToDevicePublicUserDTO(savedDevicePublicUser);
    }

    private boolean validateDevicePublicUser(DevicePublicUserDTO devicePublicUserDTO) throws ExecutionException, InterruptedException {


        CompletableFuture<Boolean> deviceExists = deviceRepository.deviceExists(devicePublicUserDTO.getDeviceId());
        CompletableFuture<Boolean> publicUserExists = publicUserRepository.publicUserExist(devicePublicUserDTO.getPublicUserId());

        CompletableFuture.allOf(deviceExists, publicUserExists);

        return deviceExists.get() && publicUserExists.get();
    }

    @Override
    public List<DevicePublicUserDTO> getByDeviceId(String deviceId) {
        return devicePublicUserRepository
                .findByDeviceId(deviceId)
                .stream()
                .map(this::mapDevicePublicUserToDevicePublicUserDTO)
                .toList();
    }

    @Override
    public Optional<DevicePublicUserDTO> get(DevicePublicUserDTO devicePublicUserDTO) {
        DevicePublicUserId devicePublicUserId = new DevicePublicUserId();
        devicePublicUserId.setDeviceId(devicePublicUserDTO.getDeviceId());
        devicePublicUserId.setPublicUserId(devicePublicUserDTO.getPublicUserId());

        return devicePublicUserRepository
                .findById(devicePublicUserId)
                .map(this::mapDevicePublicUserToDevicePublicUserDTO);
    }

    private DevicePublicUserDTO mapDevicePublicUserToDevicePublicUserDTO(DevicePublicUser devicePublicUser) {
        DevicePublicUserDTO devicePublicUserDTO = new DevicePublicUserDTO();
        devicePublicUserDTO.setDeviceId(devicePublicUser.getDeviceId());
        devicePublicUserDTO.setPublicUserId(devicePublicUser.getPublicUserId());
        return devicePublicUserDTO;
    }

    private DevicePublicUser mapDevicePublicUserDTO_ToDevicePublicUser(DevicePublicUserDTO devicePublicUserDTO) {
        DevicePublicUser devicePublicUser = new DevicePublicUser();
        devicePublicUser.setDeviceId(devicePublicUserDTO.getDeviceId());
        devicePublicUser.setPublicUserId(devicePublicUserDTO.getPublicUserId());
        return devicePublicUser;
    }
}
