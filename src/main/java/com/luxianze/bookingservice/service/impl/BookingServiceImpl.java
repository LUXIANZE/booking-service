package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.entity.Booking;
import com.luxianze.bookingservice.entity.Session;
import com.luxianze.bookingservice.repository.BookingRepository;
import com.luxianze.bookingservice.repository.SessionRepository;
import com.luxianze.bookingservice.service.BookingService;
import com.luxianze.bookingservice.service.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final SessionRepository sessionRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, SessionRepository sessionRepository) {
        this.bookingRepository = bookingRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public BookingDTO create(BookingDTO bookingDTO) throws Exception {
        boolean isSessionFull = checkIsSessionFullyBooked(bookingDTO.getSessionId());

        if (isSessionFull) {
            throw new Exception("Session is fully booked");
        }

        Booking booking = mapBookingDTO_ToBooking(bookingDTO);
        Booking savedBooking = bookingRepository.save(booking);

        return mapBookingToBookingDTO(savedBooking);
    }

    private boolean checkIsSessionFullyBooked(Long sessionId) throws Exception {
        CompletableFuture<Page<Booking>> bookingPageCompletableFuture = bookingRepository.findAllBySessionId(Pageable.unpaged(), sessionId);
        CompletableFuture<Optional<Session>> optionalSessionCompletableFuture = sessionRepository.asyncFindById(sessionId);

        CompletableFuture.allOf(bookingPageCompletableFuture, optionalSessionCompletableFuture);
        Page<Booking> bookingPage = bookingPageCompletableFuture.get();
        Optional<Session> optionalSession = optionalSessionCompletableFuture.get();

        if (optionalSession.isPresent()) {
            return bookingPage.getTotalElements() >= optionalSession.get().getTotalSlots();
        }

        throw new Exception("Unable to check session occupancy");
    }

    @Override
    public BookingDTO update(BookingDTO bookingDTO) {
        Booking booking;

        if (Objects.isNull(bookingDTO.getId())) {
            booking = mapBookingDTO_ToBooking(bookingDTO);
        } else {
            booking = bookingRepository
                    .findById(bookingDTO.getId())
                    .map(foundBooking -> this.updateBooking(foundBooking, bookingDTO))
                    .orElse(mapBookingDTO_ToBooking(bookingDTO));
        }

        Booking savedBooking = bookingRepository.save(booking);
        return mapBookingToBookingDTO(savedBooking);
    }

    private Booking updateBooking(Booking foundBooking, BookingDTO bookingDTO) {
        foundBooking.setId(bookingDTO.getId());
        foundBooking.setUserId(bookingDTO.getUserId());
        foundBooking.setSessionId(bookingDTO.getSessionId());
        foundBooking.setAttendance(bookingDTO.isAttendance());
        foundBooking.setPaymentDone(bookingDTO.isPaymentDone());

        return foundBooking;
    }

    @Override
    public Page<BookingDTO> getAll(Pageable pageable, Long sessionId) throws ExecutionException, InterruptedException {
        return bookingRepository
                .findAllBySessionId(pageable, sessionId)
                .get()
                .map(this::mapBookingToBookingDTO);
    }

    @Override
    public Optional<BookingDTO> getById(Long id) {
        return bookingRepository
                .findById(id)
                .map(this::mapBookingToBookingDTO);
    }

    private BookingDTO mapBookingToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setUserId(booking.getUserId());
        bookingDTO.setSessionId(booking.getSessionId());
        bookingDTO.setAttendance(booking.isAttendance());
        bookingDTO.setPaymentDone(booking.isPaymentDone());

        return bookingDTO;
    }

    private Booking mapBookingDTO_ToBooking(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());
        booking.setUserId(bookingDTO.getUserId());
        booking.setSessionId(bookingDTO.getSessionId());
        booking.setAttendance(bookingDTO.isAttendance());
        booking.setPaymentDone(bookingDTO.isPaymentDone());

        return booking;
    }
}
