package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.entity.DeviceUser;
import com.luxianze.bookingservice.repository.DeviceRepository;
import com.luxianze.bookingservice.repository.DeviceUserRepository;
import com.luxianze.bookingservice.repository.UserRepository;
import com.luxianze.bookingservice.service.DeviceUserService;
import com.luxianze.bookingservice.service.dto.DeviceUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class DeviceUserServiceImpl implements DeviceUserService {

    private final DeviceUserRepository deviceUserRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public DeviceUserServiceImpl(DeviceUserRepository deviceUserRepository, DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceUserRepository = deviceUserRepository;
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DeviceUserDTO save(DeviceUserDTO deviceUserDTO) throws Exception {
        boolean isDevicePublicValid = validateDeviceUser(deviceUserDTO);

        if (!isDevicePublicValid) {
            throw new Exception("Invalid DeviceUser");
        }

        DeviceUser deviceUser = mapDeviceUserDTO_ToDeviceUser(deviceUserDTO);
        DeviceUser savedDeviceUser = deviceUserRepository.save(deviceUser);

        return mapDeviceUserToDeviceUserDTO(savedDeviceUser);
    }

    private boolean validateDeviceUser(DeviceUserDTO deviceUserDTO) throws ExecutionException, InterruptedException {

        CompletableFuture<Boolean> deviceExists = deviceRepository.deviceExists(deviceUserDTO.getDeviceId());
        CompletableFuture<Boolean> userExists = userRepository.userExists(deviceUserDTO.getUserId());

        CompletableFuture.allOf(deviceExists, userExists);

        return deviceExists.get() && userExists.get();
    }

    @Override
    public List<DeviceUserDTO> getByDeviceId(String deviceId) {
        return deviceUserRepository
                .findAllByDeviceId(deviceId)
                .stream()
                .map(this::mapDeviceUserToDeviceUserDTO)
                .toList();
    }

    @Override
    public Optional<DeviceUserDTO> get(Long deviceUserId) {

        return deviceUserRepository
                .findById(deviceUserId)
                .map(this::mapDeviceUserToDeviceUserDTO);
    }

    private DeviceUserDTO mapDeviceUserToDeviceUserDTO(DeviceUser deviceUser) {
        DeviceUserDTO deviceUserDTO = new DeviceUserDTO();
        deviceUserDTO.setId(deviceUser.getId());
        deviceUserDTO.setDeviceId(deviceUser.getDeviceId());
        deviceUserDTO.setUserId(deviceUser.getUserId());
        return deviceUserDTO;
    }

    private DeviceUser mapDeviceUserDTO_ToDeviceUser(DeviceUserDTO deviceUserDTO) {
        DeviceUser deviceUser = new DeviceUser();
        deviceUser.setId(deviceUserDTO.getId());
        deviceUser.setDeviceId(deviceUserDTO.getDeviceId());
        deviceUser.setUserId(deviceUserDTO.getUserId());
        return deviceUser;
    }
}
