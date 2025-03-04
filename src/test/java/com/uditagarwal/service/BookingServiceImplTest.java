package com.uditagarwal.service;

import com.uditagarwal.exception.BadRequestException;
import com.uditagarwal.model.*;
import com.uditagarwal.model.theatre.Seat;
import com.uditagarwal.model.theatre.SeatType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * Test concurrency by attempting to book the same seats with multiple threads.
     */
    @Test
    public void testConcurrencyForSeatBooking() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        AtomicInteger successfulBookings = new AtomicInteger(0);
        AtomicInteger failedBookings = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            final String userId = "user" + i;
            executorService.submit(() -> {
                try {
                    bookingService.createBooking(showId, availableSeats, userId);
                    successfulBookings.incrementAndGet();
                } catch (BadRequestException e) {
                    failedBookings.incrementAndGet();
                    System.out.println("Booking failed for " + userId + ": " + e.getMessage());
                }
            });
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Not all tasks completed. Forcing shutdown...");
            executorService.shutdownNow();
        }

        assertEquals(1, successfulBookings.get(), "Only one booking should be successful.");
        assertEquals(numberOfThreads - 1, failedBookings.get(), "Remaining bookings should fail.");
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
        // Arrange: Create and confirm an initial booking
        Booking initialBooking = bookingService.createBooking(showId, availableSeats, userId);

        // Act: Confirm the booking to change status to CONFIRMED
        boolean isConfirmed = bookingService.confirmBooking(initialBooking, userId);
        assertTrue(isConfirmed, "Booking should be confirmed");

        // Act & Assert: Attempting to book the same seats should fail with a booked seat exception
        Exception exception = assertThrows(BadRequestException.class, () -> {
            bookingService.createBooking(showId, availableSeats, "newUser");
        });

        assertEquals("One or more seats are already booked.", exception.getMessage());
    }
}
