package com.luxianze.bookingservice.service.impl;

import com.luxianze.bookingservice.entity.Booking;
import com.luxianze.bookingservice.repository.BookingRepository;
import com.luxianze.bookingservice.service.BookingService;
import com.luxianze.bookingservice.service.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDTO create(BookingDTO bookingDTO) {
        Booking booking = mapBookingDTO_ToBooking(bookingDTO);
        Booking savedBooking = bookingRepository.save(booking);
        return mapBookingToBookingDTO(savedBooking);
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
    public Page<BookingDTO> getAll(Pageable pageable) {
        return bookingRepository
                .findAll(pageable)
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
