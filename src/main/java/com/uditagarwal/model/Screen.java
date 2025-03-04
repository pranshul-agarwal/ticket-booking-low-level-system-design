package com.uditagarwal.model;

import java.util.concurrent.CopyOnWriteArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class Screen {
    @NonNull
    private UUID id;
    private final String name;
    private final Theatre theatre;
    //Other screen metadata.
    private List<Seat> seats = new CopyOnWriteArrayList<>();

    public synchronized boolean addSeat(Seat seat) {
        return seats.add(seat);
    }
}