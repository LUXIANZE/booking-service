package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.constant.entity.Role;
import com.luxianze.bookingservice.entity.PublicUser;
import com.luxianze.bookingservice.repository.PublicUserRepository;
import com.luxianze.bookingservice.service.DevicePublicUserService;
import com.luxianze.bookingservice.service.PublicUserService;
import com.luxianze.bookingservice.service.dto.DevicePublicUserDTO;
import com.luxianze.bookingservice.service.dto.PublicUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PublicUserServiceImpl implements PublicUserService {

    private final PublicUserRepository publicUserRepository;
    private final DevicePublicUserService devicePublicUserService;

    public PublicUserServiceImpl(PublicUserRepository publicUserRepository, DevicePublicUserService devicePublicUserService) {
        this.publicUserRepository = publicUserRepository;
        this.devicePublicUserService = devicePublicUserService;
    }

    @Override
    public PublicUserDTO savePublicUser(PublicUserDTO publicUserDTO) {

        /* all public user should be of role public */
        publicUserDTO.setRole(Role.PUBLIC);

        PublicUser publicUser = mapPublicUserDTO_ToPublicUser(publicUserDTO);
        PublicUser savedPublicUser = publicUserRepository.save(publicUser);

        return mapPublicUserToPublicUserDTO(savedPublicUser);
    }

    @Override
    public PublicUserDTO updatePublicUser(PublicUserDTO publicUserDTO) {
        PublicUser publicUser;

        if (Objects.isNull(publicUserDTO.getId())) {
            publicUser = mapPublicUserDTO_ToPublicUser(publicUserDTO);
        } else {
            publicUser = publicUserRepository
                    .findById(publicUserDTO.getId())
                    .orElse(mapPublicUserDTO_ToPublicUser(publicUserDTO));
        }

        PublicUser savedPublicUser = publicUserRepository.save(publicUser);
        return mapPublicUserToPublicUserDTO(savedPublicUser);
    }

    private PublicUserDTO mapPublicUserToPublicUserDTO(PublicUser savedPublicUser) {
        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setId(savedPublicUser.getId());
        publicUserDTO.setIdentity(savedPublicUser.getIdentity());
        publicUserDTO.setPhoneNumber(savedPublicUser.getPhoneNumber());
        publicUserDTO.setEmail(savedPublicUser.getEmail());
        publicUserDTO.setRole(savedPublicUser.getRole());

        return publicUserDTO;
    }

    private PublicUser mapPublicUserDTO_ToPublicUser(PublicUserDTO publicUserDTO) {
        PublicUser publicUser = new PublicUser();
        publicUser.setId(publicUserDTO.getId());
        publicUser.setIdentity(publicUserDTO.getIdentity());
        publicUser.setPhoneNumber(publicUserDTO.getPhoneNumber());
        publicUser.setEmail(publicUserDTO.getEmail());
        publicUser.setRole(Role.PUBLIC);

        return publicUser;
    }

    @Override
    public Optional<PublicUserDTO> getPublicUserById(Long publicUserId) {
        return publicUserRepository
                .findById(publicUserId)
                .map(this::mapPublicUserToPublicUserDTO);
    }

    @Override
    public List<PublicUserDTO> getPublicUserListByDeviceId(String deviceId) {
        List<DevicePublicUserDTO> devicePublicUserDTOList = devicePublicUserService.getByDeviceId(deviceId);
        return devicePublicUserDTOList
                .parallelStream()
                .map(devicePublicUserDTO -> publicUserRepository.findById(devicePublicUserDTO.getPublicUserId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::mapPublicUserToPublicUserDTO)
                .toList();
    }
}
