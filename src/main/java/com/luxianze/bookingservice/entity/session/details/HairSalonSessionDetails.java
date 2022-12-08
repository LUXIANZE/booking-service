package com.luxianze.bookingservice.entity.session.details;

import com.luxianze.bookingservice.constant.entity.HairSalonServices;

import javax.persistence.*;

@Entity
public class HairSalonSessionDetails {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double durationInHours;

    @Column
    private double price;

    @Column
    private String currency;

    @Column
    @Enumerated
    private HairSalonServices hairSalonServices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(double durationInHours) {
        this.durationInHours = durationInHours;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public HairSalonServices getHairSalonServices() {
        return hairSalonServices;
    }

    public void setHairSalonServices(HairSalonServices hairSalonService) {
        this.hairSalonServices = hairSalonService;
    }
}
