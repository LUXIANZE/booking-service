package com.luxianze.bookingservice.entity.composite.keys;

import java.io.Serializable;

public class DevicePublicUserId implements Serializable {
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
