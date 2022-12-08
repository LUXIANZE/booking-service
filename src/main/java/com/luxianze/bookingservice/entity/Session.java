package com.luxianze.bookingservice.entity;

import com.luxianze.bookingservice.constant.entity.SessionType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Session {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime dateTime;

    @Column
    private int slots;

    @Column
    @Enumerated
    private SessionType sessionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }
}
