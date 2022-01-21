package com.uditagarwal.api;

import com.uditagarwal.services.BookingService;
import com.uditagarwal.services.PaymentsService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class PaymentsController {
    private final PaymentsService paymentsService;
    private final BookingService bookingService;

    public void paymentFailed(@NonNull final String bookingId, @NonNull final String user) {
        paymentsService.processPaymentFailed(bookingService.getBooking(bookingId), user);
    }

    public void paymentSuccess(@NonNull final  String bookingId, @NonNull final String user) {
        bookingService.confirmBooking(bookingService.getBooking(bookingId), user);
    }

}
