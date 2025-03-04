package com.uditagarwal.service;

import com.uditagarwal.model.Artist;
import com.uditagarwal.model.Movie;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MovieService {

    private final Map<UUID, Movie> movies = new ConcurrentHashMap<>();

    public Movie createMovie(@NonNull final String name,
                             final int duration,
                             @NonNull final String language,
                             @NonNull final List<String> genres,
                             @NonNull final List<Artist> artists) {
        Movie movie = Movie.builder()
                .id(UUID.randomUUID())
                .name(name)
                .duration(duration)
                .language(language)
                .genres(genres)
                .artists(artists)
                .build();

        movies.put(movie.getId(), movie);
        return movie;
    }

    public Movie getMovie(@NonNull final UUID movieId) {
        return Optional.ofNullable(movies.get(movieId))
                .orElseThrow(() -> new NoSuchElementException("Movie not found"));
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }
}
