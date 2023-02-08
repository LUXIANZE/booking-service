package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.DeviceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceUserRepository extends JpaRepository<DeviceUser, Long> {
    List<DeviceUser> findAllByDeviceId(String deviceId);
}
