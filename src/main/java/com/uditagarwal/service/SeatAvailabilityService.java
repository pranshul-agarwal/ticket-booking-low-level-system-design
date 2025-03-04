package com.uditagarwal.service;

import com.uditagarwal.model.theatre.Seat;
import com.uditagarwal.model.theatre.ShowSeatLock;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SeatAvailabilityService {

    private final Map<UUID, Map<UUID, ShowSeatLock>> seatLocks = new ConcurrentHashMap<>();

    /**
     * Attempts to lock a list of seats for a specific show and user.
     *
     * @param showId          The ID of the show.
     * @param seats           The list of seats to lock.
     * @param userId          The ID of the user requesting the lock.
     * @param timeoutInSeconds The lock duration in seconds.
     * @return true if all seats were locked successfully, false otherwise.
     */
    public synchronized boolean lockSeats(@NonNull final UUID showId,
                                          @NonNull final List<Seat> seats,
                                          @NonNull final String userId,
                                          final int timeoutInSeconds) {
        Map<UUID, ShowSeatLock> locks = seatLocks.computeIfAbsent(showId, k -> new ConcurrentHashMap<>());

        for (Seat seat : seats) {
            if (locks.containsKey(seat.getId())) {
                return false; // If any seat is already locked, abort the operation
            }
        }

        for (Seat seat : seats) {
            locks.put(seat.getId(), new ShowSeatLock(UUID.randomUUID(), showId, seat, userId,
                    LocalDateTime.now(), timeoutInSeconds));
        }
        return true;
    }

    public synchronized boolean unlockSeats(@NonNull final UUID showId,
                                            @NonNull final List<Seat> seats,
                                            @NonNull final String userId) {
        Map<UUID, ShowSeatLock> locks = seatLocks.get(showId);
        if (locks != null) {
            for (Seat seat : seats) {
                ShowSeatLock lock = locks.get(seat.getId());
                if (lock != null && lock.getLockedBy().equals(userId)) {
                    locks.remove(seat.getId());
                }
            }
            return true;
        }
        return false;
    }

    public boolean isSeatLocked(@NonNull final UUID showId, @NonNull final UUID seatId) {
        Map<UUID, ShowSeatLock> locks = seatLocks.get(showId);
        if (locks != null) {
            ShowSeatLock lock = locks.get(seatId);
            return lock != null && !lock.isExpired();
        }
        return false;
    }
}
