package com.luxianze.bookingservice.controller.session;

import com.luxianze.bookingservice.service.BadmintonTrainingSessionDetailsService;
import com.luxianze.bookingservice.service.dto.BadmintonTrainingSessionDetailsDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("badminton-training-session-details")
public class BadmintonTrainingSessionDetailsController {

    private final BadmintonTrainingSessionDetailsService badmintonTrainingSessionDetailsService;

    public BadmintonTrainingSessionDetailsController(BadmintonTrainingSessionDetailsService badmintonTrainingSessionDetailsService) {
        this.badmintonTrainingSessionDetailsService = badmintonTrainingSessionDetailsService;
    }

    @GetMapping
    public ResponseEntity<Page<BadmintonTrainingSessionDetailsDTO>> getAll(Pageable pageable) {
        return ResponseEntity
                .ok(badmintonTrainingSessionDetailsService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadmintonTrainingSessionDetailsDTO> getById(@PathVariable Long id) {
        return badmintonTrainingSessionDetailsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sessionId/{id}")
    public ResponseEntity<BadmintonTrainingSessionDetailsDTO> getBySessionId(@PathVariable Long id) {
        return badmintonTrainingSessionDetailsService.findBySessionId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @SecurityRequirement(name = "bearer-key")
    @PreAuthorize("hasAnyAuthority({'SCOPE_SUPERUSER','SCOPE_ADMIN', 'SCOPE_COACH', 'SCOPE_TEACHER'})")
    public ResponseEntity<BadmintonTrainingSessionDetailsDTO> create(@RequestBody BadmintonTrainingSessionDetailsDTO badmintonTrainingSessionDetailsDTO) throws Exception {
        return ResponseEntity
                .ok(badmintonTrainingSessionDetailsService.create(badmintonTrainingSessionDetailsDTO));
    }

    @PutMapping
    @SecurityRequirement(name = "bearer-key")
    @PreAuthorize("hasAnyAuthority({'SCOPE_SUPERUSER','SCOPE_ADMIN', 'SCOPE_COACH', 'SCOPE_TEACHER'})")
    public ResponseEntity<BadmintonTrainingSessionDetailsDTO> update(@RequestBody BadmintonTrainingSessionDetailsDTO badmintonTrainingSessionDetailsDTO) throws Exception {
        return ResponseEntity
                .ok(badmintonTrainingSessionDetailsService.update(badmintonTrainingSessionDetailsDTO));
    }
}
