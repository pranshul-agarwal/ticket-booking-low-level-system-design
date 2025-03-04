package com.uditagarwal.service;

import com.uditagarwal.model.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {

    private final Map<UUID, Payment> payments = new ConcurrentHashMap<>();

    public synchronized Payment initiatePayment(@NonNull final UUID bookingId,
                                                final double amount,
                                                @NonNull final String userId) {
        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .status(PaymentStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .amount(amount)
                .build();
        payments.put(payment.getId(), payment);
        return payment;
    }

    public synchronized boolean handleFailedPayment(@NonNull final UUID paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment != null) {
            payment.setStatus(PaymentStatus.CANCELLED);
            return true;
        }
        return false;
    }

    public synchronized boolean retryPayment(@NonNull final UUID paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment != null && payment.getStatus() == PaymentStatus.CANCELLED) {
            payment.setStatus(PaymentStatus.PENDING);
            return true;
        }
        return false;
    }
}
