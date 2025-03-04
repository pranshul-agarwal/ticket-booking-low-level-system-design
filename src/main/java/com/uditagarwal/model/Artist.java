package com.uditagarwal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    @NonNull private UUID id;
    @NonNull private String name;
    private String description;
    private String url;
}
