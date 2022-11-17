package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByIdentity(String identity);
    List<User> findAllByIdentity(String identity);
}
