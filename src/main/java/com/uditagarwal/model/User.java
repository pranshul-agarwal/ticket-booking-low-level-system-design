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
public class User {
    @NonNull private UUID id;
    @NonNull private String name;
    @NonNull private String email;
    @NonNull private String pincode;
    @NonNull private String city;
}

