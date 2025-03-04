package com.uditagarwal.model;

import com.uditagarwal.exception.InvalidStateException;
import lombok.NonNull;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @NonNull private UUID id;
    @NonNull private User user;
    @NonNull private UUID showId;
    private Payment payment;
    private List<Seat> bookedSeats = new CopyOnWriteArrayList<>();
    @NonNull private BookingStatus bookingStatus;
    private double totalAmount;

    public List<Seat> getBookedSeats() {
        return Collections.unmodifiableList(bookedSeats);
    }

//    public void confirmBooking() {
//        if (this.bookingStatus != BookingStatus.PENDING) {
//            throw new InvalidStateException();
//        }
//        this.bookingStatus = BookingStatus.Confirmed;
//    }
//
//    public void expireBooking() {
//        if (this.bookingStatus != BookingStatus.Created) {
//            throw new InvalidStateException();
//        }
//        this.bookingStatus = BookingStatus.Expired;
//    }
}
