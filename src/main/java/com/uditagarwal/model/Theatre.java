package com.uditagarwal.model;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Theatre {

    private final String theatreId;
    private final String name;
    private final List<Screen> screens;
    //Other theatre metadata.

    public Theatre(@NonNull final String theatreId, @NonNull final String name) {
        this.theatreId = theatreId;
        this.name = name;
        this.screens = new ArrayList<>();
    }

    public void addScreen(@NonNull final  Screen screen) {
        screens.add(screen);
    }
}
