package com.uditagarwal.model.theatre;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Show {
    @NonNull
    private UUID id;
    @NonNull private String movieId;
    @NonNull private String screenId;
    @NonNull private LocalDateTime startTime;
    private int duration; // in minutes
    private Map<SeatType, Double> prices = new ConcurrentHashMap<>();

    public Map<SeatType, Double> getPrices() {
        return Collections.unmodifiableMap(prices);
    }
}