package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.entity.Session;
import com.luxianze.bookingservice.repository.SessionRepository;
import com.luxianze.bookingservice.service.SessionService;
import com.luxianze.bookingservice.service.dto.SessionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Page<SessionDTO> getAll(Pageable pageable) {
        return sessionRepository
                .findAll(pageable)
                .map(this::mapSessionToSessionDTO);
    }

    @Override
    public Optional<SessionDTO> getById(Long id) {
        return sessionRepository
                .findById(id)
                .map(this::mapSessionToSessionDTO);
    }

    @Override
    public SessionDTO create(SessionDTO sessionDTO) {
        Session session = mapSessionDTO_ToSession(sessionDTO);
        Session savedSession = sessionRepository.save(session);
        return mapSessionToSessionDTO(savedSession);
    }

    @Override
    public SessionDTO update(SessionDTO sessionDTO) {
        Session session;

        if (Objects.isNull(sessionDTO.getId())) {
            session = mapSessionDTO_ToSession(sessionDTO);
        } else {
            session = sessionRepository
                    .findById(sessionDTO.getId())
                    .map(foundSession -> this.updateSession(foundSession, sessionDTO))
                    .orElse(mapSessionDTO_ToSession(sessionDTO));

        }

        Session savedSession = sessionRepository.save(session);
        return mapSessionToSessionDTO(savedSession);
    }


    private SessionDTO mapSessionToSessionDTO(Session session) {
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId(session.getId());
        sessionDTO.setSessionType(session.getSessionType());
        sessionDTO.setSlots(session.getSlots());
        sessionDTO.setDateTime(session.getDateTime());
        return sessionDTO;
    }

    private Session mapSessionDTO_ToSession(SessionDTO sessionDTO) {
        Session session = new Session();
        session.setId(sessionDTO.getId());
        session.setSessionType(sessionDTO.getSessionType());
        session.setSlots(sessionDTO.getSlots());
        session.setDateTime(sessionDTO.getDateTime());
        return session;
    }

    private Session updateSession(Session session, SessionDTO sessionDTO) {
        session.setDateTime(sessionDTO.getDateTime());
        session.setSessionType(sessionDTO.getSessionType());
        session.setSlots(sessionDTO.getSlots());

        return session;
    }
}
