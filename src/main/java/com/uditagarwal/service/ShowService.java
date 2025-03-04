package com.uditagarwal.service;

import com.uditagarwal.model.theatre.SeatType;
import com.uditagarwal.model.theatre.Show;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShowService {

    private final Map<UUID, Show> shows = new ConcurrentHashMap<>();

    public Show createShow(@NonNull final UUID showId,
                           @NonNull final UUID movieId,
                           @NonNull final UUID screenId,
                           @NonNull final LocalDateTime startTime,
                           final int duration,
                           @NonNull final Map<SeatType, Double> prices) {
        Show show = Show.builder()
                .id(showId)
                .movieId(movieId.toString())
                .screenId(screenId.toString())
                .startTime(startTime)
                .duration(duration)
                .prices(prices)
                .build();

        shows.put(show.getId(), show);
        return show;
    }

    public Show getShow(@NonNull final UUID showId) {
        return Optional.ofNullable(shows.get(showId))
                .orElseThrow(() -> new NoSuchElementException("Show not found"));
    }

    public List<Show> getShowsByMovie(@NonNull final UUID movieId) {
        List<Show> showList = new ArrayList<>();
        shows.values().forEach(show -> {
            if (show.getMovieId().equals(movieId.toString())) {
                showList.add(show);
            }
        });
        return showList;
    }
}
