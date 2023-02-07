package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByIdentity(String identity);
    Optional<User> findOneByIdentity(String identity);
    boolean existsByIdentity(String identity);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
}
