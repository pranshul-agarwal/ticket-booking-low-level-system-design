package com.uditagarwal.model.theatre;

import lombok.NonNull;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Theatre {
    @NonNull private UUID id;
    @NonNull private String name;
    @NonNull private String city;
    @NonNull private String pincode;
    private List<Screen> screens = new CopyOnWriteArrayList<>();

    public List<Screen> getScreens() {
        return Collections.unmodifiableList(screens);
    }

    public void addScreen(@NonNull final  Screen screen) {
        screens.add(screen);
    }
}

