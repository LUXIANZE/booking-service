package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.PublicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public interface PublicUserRepository extends JpaRepository<PublicUser, Long> {
    @Async
    @Query("select case when count(pu)>0 then true else false end from PublicUser pu where pu.id = :id")
    CompletableFuture<Boolean> publicUserExist(Long id);
}
