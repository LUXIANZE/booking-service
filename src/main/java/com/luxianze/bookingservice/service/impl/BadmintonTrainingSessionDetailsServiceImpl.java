package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.constant.entity.Role;
import com.luxianze.bookingservice.constant.entity.SessionType;
import com.luxianze.bookingservice.entity.Session;
import com.luxianze.bookingservice.entity.User;
import com.luxianze.bookingservice.entity.session.details.BadmintonTrainingSessionDetails;
import com.luxianze.bookingservice.repository.SessionRepository;
import com.luxianze.bookingservice.repository.UserRepository;
import com.luxianze.bookingservice.repository.session.details.BadmintonTrainingSessionDetailsRepository;
import com.luxianze.bookingservice.service.BadmintonTrainingSessionDetailsService;
import com.luxianze.bookingservice.service.dto.BadmintonTrainingSessionDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class BadmintonTrainingSessionDetailsServiceImpl implements BadmintonTrainingSessionDetailsService {

    private final BadmintonTrainingSessionDetailsRepository badmintonTrainingSessionDetailsRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public BadmintonTrainingSessionDetailsServiceImpl(BadmintonTrainingSessionDetailsRepository badmintonTrainingSessionDetailsRepository, SessionRepository sessionRepository, UserRepository userRepository) {
        this.badmintonTrainingSessionDetailsRepository = badmintonTrainingSessionDetailsRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<BadmintonTrainingSessionDetailsDTO> findAll(Pageable pageable) {
        return badmintonTrainingSessionDetailsRepository
                .findAll(pageable)
                .map(this::toDTO);
    }

    @Override
    public Optional<BadmintonTrainingSessionDetailsDTO> findBySessionId(Long sessionId) {
        return badmintonTrainingSessionDetailsRepository
                .findBySessionId(sessionId)
                .map(this::toDTO);
    }

    @Override
    public Optional<BadmintonTrainingSessionDetailsDTO> findById(Long id) {
        return badmintonTrainingSessionDetailsRepository
                .findById(id)
                .map(this::toDTO);
    }

    @Override
    public BadmintonTrainingSessionDetailsDTO create(BadmintonTrainingSessionDetailsDTO badmintonTrainingSessionDetailsDTO) throws Exception {
        if (!Objects.isNull(badmintonTrainingSessionDetailsDTO.getId()))
            throw new Exception("New BadmintonTrainingSessionDetails cannot already have an Id.");

        validate(badmintonTrainingSessionDetailsDTO);

        BadmintonTrainingSessionDetails badmintonTrainingSessionDetails = toEntity(badmintonTrainingSessionDetailsDTO);
        BadmintonTrainingSessionDetails savedEntity = badmintonTrainingSessionDetailsRepository.save(badmintonTrainingSessionDetails);

        return toDTO(savedEntity);
    }

    @Override
    public BadmintonTrainingSessionDetailsDTO update(BadmintonTrainingSessionDetailsDTO badmintonTrainingSessionDetailsDTO) throws Exception {

        if (Objects.isNull(badmintonTrainingSessionDetailsDTO.getId()))
            return create(badmintonTrainingSessionDetailsDTO);

        BadmintonTrainingSessionDetails existingBadmintonTrainingSessionDetails = badmintonTrainingSessionDetailsRepository
                .findById(badmintonTrainingSessionDetailsDTO.getId())
                .orElseThrow(() -> new Exception("No BadmintonTrainingSessionDetails with ID: " + badmintonTrainingSessionDetailsDTO.getId() + " found."));

        existingBadmintonTrainingSessionDetails.setDurationInHours(badmintonTrainingSessionDetailsDTO.getDurationInHours());
        existingBadmintonTrainingSessionDetails.setSessionId(badmintonTrainingSessionDetailsDTO.getSessionId());
        existingBadmintonTrainingSessionDetails.setPrice(badmintonTrainingSessionDetailsDTO.getPrice());
        existingBadmintonTrainingSessionDetails.setCurrency(badmintonTrainingSessionDetailsDTO.getCurrency());
        existingBadmintonTrainingSessionDetails.setCoachId(badmintonTrainingSessionDetailsDTO.getCoachId());

        BadmintonTrainingSessionDetails updatedEntity = badmintonTrainingSessionDetailsRepository.save(existingBadmintonTrainingSessionDetails);

        return toDTO(updatedEntity);
    }

    private BadmintonTrainingSessionDetailsDTO toDTO(BadmintonTrainingSessionDetails badmintonTrainingSessionDetails) {
        BadmintonTrainingSessionDetailsDTO badmintonTrainingSessionDetailsDTO = new BadmintonTrainingSessionDetailsDTO();

        badmintonTrainingSessionDetailsDTO.setId(badmintonTrainingSessionDetails.getId());
        badmintonTrainingSessionDetailsDTO.setSessionId(badmintonTrainingSessionDetails.getSessionId());
        badmintonTrainingSessionDetailsDTO.setDurationInHours(badmintonTrainingSessionDetails.getDurationInHours());
        badmintonTrainingSessionDetailsDTO.setPrice(badmintonTrainingSessionDetails.getPrice());
        badmintonTrainingSessionDetailsDTO.setCurrency(badmintonTrainingSessionDetails.getCurrency());
        badmintonTrainingSessionDetailsDTO.setCoachId(badmintonTrainingSessionDetails.getCoachId());

        return badmintonTrainingSessionDetailsDTO;
    }

    private BadmintonTrainingSessionDetails toEntity(BadmintonTrainingSessionDetailsDTO badmintonTrainingSessionDetailsDTO) {
        BadmintonTrainingSessionDetails badmintonTrainingSessionDetails = new BadmintonTrainingSessionDetails();

        badmintonTrainingSessionDetails.setId(badmintonTrainingSessionDetailsDTO.getId());
        badmintonTrainingSessionDetails.setDurationInHours(badmintonTrainingSessionDetailsDTO.getDurationInHours());
        badmintonTrainingSessionDetails.setSessionId(badmintonTrainingSessionDetailsDTO.getSessionId());
        badmintonTrainingSessionDetails.setPrice(badmintonTrainingSessionDetailsDTO.getPrice());
        badmintonTrainingSessionDetails.setCurrency(badmintonTrainingSessionDetailsDTO.getCurrency());
        badmintonTrainingSessionDetails.setCoachId(badmintonTrainingSessionDetailsDTO.getCoachId());

        return badmintonTrainingSessionDetails;
    }

    private void validate(BadmintonTrainingSessionDetailsDTO badmintonTrainingSessionDetailsDTO) throws Exception {
        Session session = sessionRepository
                .findById(badmintonTrainingSessionDetailsDTO.getSessionId())
                .orElseThrow(() -> new Exception("Session with ID: " + badmintonTrainingSessionDetailsDTO.getSessionId() + " not found."));
        User user = userRepository
                .findById(badmintonTrainingSessionDetailsDTO.getCoachId())
                .orElseThrow(() -> new Exception("User with ID: " + badmintonTrainingSessionDetailsDTO.getCoachId() + " not found."));

        if (session.getSessionType() != SessionType.BADMINTON_TRAINING)
            throw new Exception("Session with ID: " + session.getId() + " is not a Badminton Training Session");

        if (user.getRole() != Role.COACH)
            throw new Exception("User with ID: " + user.getId() + " is not a COACH");
    }
}
