package com.uditagarwal.model.theatre;

import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @NonNull private UUID id;
    @NonNull
    private SeatType seatType;
    private int row;
    private int col;
    private AtomicReference<SeatStatus> status = new AtomicReference<>(SeatStatus.FREE);

    public boolean updateStatus(SeatStatus expectedStatus, SeatStatus newStatus) {
        return status.compareAndSet(expectedStatus, newStatus);
    }
}
