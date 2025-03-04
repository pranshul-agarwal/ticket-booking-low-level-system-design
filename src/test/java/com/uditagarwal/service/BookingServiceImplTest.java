package com.uditagarwal.service;

import com.uditagarwal.exception.BadRequestException;
import com.uditagarwal.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceImplTest {

    private BookingServiceImpl bookingService;
    private SeatAvailabilityService seatAvailabilityService;
    private ShowService showService;

    private UUID showId;
    private String userId;
    private List<Seat> availableSeats;

    @BeforeEach
    public void setup() {
        seatAvailabilityService = new SeatAvailabilityService();
        showService = new ShowService();

        bookingService = new BookingServiceImpl(seatAvailabilityService, showService);

        showId = UUID.randomUUID();
        userId = "testUser";

        availableSeats = Arrays.asList(
                Seat.builder().id(UUID.randomUUID()).seatType(SeatType.REGULAR).row(1).col(1).build(),
                Seat.builder().id(UUID.randomUUID()).seatType(SeatType.REGULAR).row(1).col(2).build()
        );

        Map<SeatType, Double> seatPrices = new HashMap<>();
        seatPrices.put(SeatType.REGULAR, 100.0);

        showService.createShow(showId, UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), 120, seatPrices);
    }

    @Test
    public void testCreateBooking_Success() {
        Booking booking = bookingService.createBooking(showId, availableSeats, userId);

        assertNotNull(booking);
        assertEquals(BookingStatus.PENDING, booking.getBookingStatus());
        assertEquals(availableSeats.size(), booking.getBookedSeats().size());
    }

    @Test
    public void testConfirmBooking_Success() {
        Booking booking = bookingService.createBooking(showId, availableSeats, userId);
        boolean result = bookingService.confirmBooking(booking, userId);

        assertTrue(result);
        assertEquals(BookingStatus.CONFIRMED, booking.getBookingStatus());
    }

    @Test
    public void testCancelBooking_Success() {
        Booking booking = bookingService.createBooking(showId, availableSeats, userId);
        boolean result = bookingService.cancelBooking(booking, userId);

        assertTrue(result);
        assertEquals(BookingStatus.CANCELED, booking.getBookingStatus());
    }

    @Test
    public void testCreateBooking_SeatAlreadyBooked() {
        Booking initialBooking = bookingService.createBooking(showId, availableSeats, userId);
        bookingService.confirmBooking(initialBooking, userId);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            bookingService.createBooking(showId, availableSeats, "newUser");
        });

        assertEquals("One or more seats are already booked.", exception.getMessage());
    }
}
