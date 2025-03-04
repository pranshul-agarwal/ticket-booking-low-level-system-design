package com.uditagarwal.model;

import com.uditagarwal.model.Artist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @NonNull private UUID id;
    @NonNull private String name;
    private int duration; // in minutes
    @NonNull private String language;
    private List<String> genres = Collections.emptyList();
    private List<Artist> artists = Collections.emptyList();

    public List<String> getGenres() {
        return Collections.unmodifiableList(genres);
    }

    public List<Artist> getArtists() {
        return Collections.unmodifiableList(artists);
    }
}
