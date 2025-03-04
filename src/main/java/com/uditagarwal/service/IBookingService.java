package com.uditagarwal.service;

import com.uditagarwal.model.Booking;
import com.uditagarwal.model.Seat;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public interface IBookingService {

    Booking createBooking(@NonNull UUID showId, @NonNull List<Seat> seats, @NonNull String userId);

    boolean confirmBooking(@NonNull Booking booking, @NonNull String userId);

    boolean cancelBooking(@NonNull Booking booking, @NonNull String userId);
}
