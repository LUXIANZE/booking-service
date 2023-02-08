package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByIdentity(String identity);

    Optional<User> findOneByIdentity(String identity);

    boolean existsByIdentity(String identity);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    @Async
    @Query("select case when count(u)>0 then true else false end from User u where u.id = :id")
    CompletableFuture<Boolean> userExists(Long id);
}
