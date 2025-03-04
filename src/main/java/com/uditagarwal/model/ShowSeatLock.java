package com.uditagarwal.model;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ShowSeatLock {
    @NonNull private UUID id;
    @NonNull private UUID showId;
    @NonNull private Seat seat;
    @NonNull private String lockedBy; // User who locked the seat
    @NonNull private LocalDateTime lockedAt; // Lock creation time
    private int timeoutInSeconds; // Lock timeout duration

    /**
     * Checks if the lock has expired based on the timeout duration.
     *
     * @return true if the lock has expired, false otherwise.
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(lockedAt.plusSeconds(timeoutInSeconds));
    }
}
