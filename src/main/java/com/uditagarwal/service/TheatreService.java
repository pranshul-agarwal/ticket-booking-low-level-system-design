package com.uditagarwal.service;

import com.uditagarwal.model.Screen;
import com.uditagarwal.model.Theatre;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TheatreService {

    private final Map<UUID, Theatre> theatres = new ConcurrentHashMap<>();
    private final Map<UUID, Screen> screens = new ConcurrentHashMap<>();

    public Theatre createTheatre(@NonNull final String city,
                                 @NonNull final String pincode,
                                 @NonNull final List<Screen> screens) {
        Theatre theatre = Theatre.builder()
                .id(UUID.randomUUID())
                .city(city)
                .pincode(pincode)
                .screens(screens)
                .build();

        theatres.put(theatre.getId(), theatre);
        screens.forEach(screen -> this.screens.put(screen.getId(), screen));
        return theatre;
    }

    public Theatre getTheatre(@NonNull final UUID theatreId) {
        return Optional.ofNullable(theatres.get(theatreId))
                .orElseThrow(() -> new NoSuchElementException("Theatre not found"));
    }

    public Screen getScreen(@NonNull final UUID screenId) {
        return Optional.ofNullable(screens.get(screenId))
                .orElseThrow(() -> new NoSuchElementException("Screen not found"));
    }
}
