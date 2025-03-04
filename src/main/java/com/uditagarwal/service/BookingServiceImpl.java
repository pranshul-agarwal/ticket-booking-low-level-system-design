package com.uditagarwal.service;

import com.uditagarwal.exception.BadRequestException;
import com.uditagarwal.model.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookingServiceImpl implements IBookingService {

    private final Map<UUID, Booking> bookings = new ConcurrentHashMap<>();
    private final SeatAvailabilityService seatAvailabilityService;
    private final ShowService showService;

    @Autowired
    public BookingServiceImpl(@NonNull final SeatAvailabilityService seatAvailabilityService,
                              @NonNull final ShowService showService) {
        this.seatAvailabilityService = seatAvailabilityService;
        this.showService = showService;
    }

    @Override
    public synchronized Booking createBooking(@NonNull final UUID showId,
                                              @NonNull final List<Seat> seats,
                                              @NonNull final String userId) {
        Show show = showService.getShow(showId);

        // Step 1: Check if any seats are already booked before attempting to lock them
        if (isAnySeatAlreadyBooked(show, seats)) {
            throw new BadRequestException("One or more seats are already booked.");
        }

        // Step 2: Attempt to lock the seats
        if (!seatAvailabilityService.lockSeats(showId, seats, userId, 300)) {
            throw new BadRequestException("Some seats are not available.");
        }

        // Step 3: Proceed with booking creation if all seats are locked
        Booking booking = Booking.builder()
                .id(UUID.randomUUID())
                .showId(showId)
                .user(new User(UUID.randomUUID(), userId, userId + "@example.com", "560001", "Bangalore"))
                .bookedSeats(seats)
                .bookingStatus(BookingStatus.PENDING)
                .totalAmount(seats.size() * 100.0)
                .build();

        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public synchronized boolean confirmBooking(@NonNull final Booking booking, @NonNull final String userId) {
        if (booking.getUser().getName().equals(userId) &&
                booking.getBookingStatus() == BookingStatus.PENDING) {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookings.put(booking.getId(), booking); // Ensure the updated booking is stored
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean cancelBooking(@NonNull final Booking booking, @NonNull final String userId) {
        if (booking.getUser().getName().equals(userId)) {
            booking.setBookingStatus(BookingStatus.CANCELED);
            seatAvailabilityService.unlockSeats(booking.getShowId(), booking.getBookedSeats(), userId);
            return true;
        }
        return false;
    }

    /**
     * Checks if any of the requested seats are already booked for the show.
     */
    private boolean isAnySeatAlreadyBooked(final Show show, final List<Seat> seats) {
        final List<Seat> bookedSeats = getBookedSeats(show);
        for (Seat seat : seats) {
            if (bookedSeats.contains(seat)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all booked seats for a particular show.
     */
    private List<Seat> getBookedSeats(final Show show) {
        List<Seat> bookedSeats = new ArrayList<>();
        bookings.values().forEach(booking -> {
            if (booking.getShowId().equals(show.getId()) &&
                    booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                bookedSeats.addAll(booking.getBookedSeats());
            }
        });
        return bookedSeats;
    }
}
