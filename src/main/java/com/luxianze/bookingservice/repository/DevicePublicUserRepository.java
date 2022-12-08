package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.DevicePublicUser;
import com.luxianze.bookingservice.entity.composite.keys.DevicePublicUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevicePublicUserRepository extends JpaRepository<DevicePublicUser, DevicePublicUserId> {
    List<DevicePublicUser> findByDeviceId(String deviceId);
    Optional<DevicePublicUser> findByPublicUserId(Long publicUserId);
}
