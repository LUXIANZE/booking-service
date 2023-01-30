package com.luxianze.bookingservice.controller.session;

import com.luxianze.bookingservice.service.SessionService;
import com.luxianze.bookingservice.service.dto.SessionDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<Page<SessionDTO>> getAll(Pageable pageable, @RequestParam String dateString) {
        return ResponseEntity
                .ok(sessionService.getAll(pageable, LocalDate.parse(dateString)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getById(@PathVariable Long id) {
        return sessionService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @SecurityRequirement(name = "bearer-key")
    @PostMapping
    @PreAuthorize("hasAnyAuthority({'SCOPE_SUPERUSER','SCOPE_ADMIN', 'SCOPE_COACH', 'SCOPE_TEACHER'})")
    public ResponseEntity<SessionDTO> create(@RequestBody SessionDTO sessionDTO) {
        return ResponseEntity
                .ok(sessionService.create(sessionDTO));
    }

    @SecurityRequirement(name = "bearer-key")
    @PutMapping
    @PreAuthorize("hasAnyAuthority({'SCOPE_SUPERUSER','SCOPE_ADMIN', 'SCOPE_COACH', 'SCOPE_TEACHER'})")
    public ResponseEntity<SessionDTO> update(@RequestBody SessionDTO sessionDTO) {
        return ResponseEntity
                .ok(sessionService.update(sessionDTO));
    }
}