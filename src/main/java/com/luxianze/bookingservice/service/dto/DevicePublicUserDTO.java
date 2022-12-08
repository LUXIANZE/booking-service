package com.luxianze.bookingservice.service.dto;

import lombok.Data;

@Data
public class DevicePublicUserDTO {
    private String deviceId;
    private Long publicUserId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getPublicUserId() {
        return publicUserId;
    }

    public void setPublicUserId(Long publicUserId) {
        this.publicUserId = publicUserId;
    }
}
