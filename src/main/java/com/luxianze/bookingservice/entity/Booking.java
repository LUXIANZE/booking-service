package com.luxianze.bookingservice.entity;

import javax.persistence.*;

@Entity
public class Booking {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long sessionId;

    @Column
    private boolean paymentDone;

    @Column
    private boolean attendance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    public boolean isAttendance() {
        return attendance;
    }

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }
}
