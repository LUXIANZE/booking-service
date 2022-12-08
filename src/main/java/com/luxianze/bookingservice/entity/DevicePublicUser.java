package com.luxianze.bookingservice.entity;

import com.luxianze.bookingservice.entity.composite.keys.DevicePublicUserId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(DevicePublicUserId.class)
public class DevicePublicUser {
    @Id
    private String deviceId;

    @Id
    private Long publicUserId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String accountId) {
        this.deviceId = accountId;
    }

    public Long getPublicUserId() {
        return publicUserId;
    }

    public void setPublicUserId(Long publicUserId) {
        this.publicUserId = publicUserId;
    }
}
