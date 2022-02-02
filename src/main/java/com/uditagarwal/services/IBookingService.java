package com.uditagarwal.services;

import com.uditagarwal.model.Booking;
import com.uditagarwal.model.Seat;
import com.uditagarwal.model.Show;
import lombok.NonNull;

import java.util.List;

public interface IBookingService {
    public Booking getBooking(@NonNull final String bookingId);
    public List<Booking> getAllBookings(@NonNull final Show show);
    public Booking createBooking(@NonNull final String userId, @NonNull final Show show,
                                 @NonNull final List<Seat> seats);
    public List<Seat> getBookedSeats(@NonNull final Show show);
    public void confirmBooking(@NonNull final Booking booking, @NonNull final String user);
}
