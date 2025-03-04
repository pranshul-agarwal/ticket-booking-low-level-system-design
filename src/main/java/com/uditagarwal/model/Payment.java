package com.uditagarwal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @NonNull private UUID id;
    @NonNull private Booking booking;
    @NonNull private PaymentStatus status;
    @NonNull private LocalDateTime timestamp;
    private double amount;
}

