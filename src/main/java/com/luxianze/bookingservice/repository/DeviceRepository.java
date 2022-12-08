package com.luxianze.bookingservice.repository;

import com.luxianze.bookingservice.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    @Async
    @Query("select case when count(d)>0 then true else false end from Device d where d.id = :id")
    CompletableFuture<Boolean> deviceExists(String id);
}
