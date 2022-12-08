package com.luxianze.bookingservice.entity;

import com.luxianze.bookingservice.constant.entity.Role;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"identity"})
)
public class PublicUser {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User IC
     */
    @Column
    private String identity;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
