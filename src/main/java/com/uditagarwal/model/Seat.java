package com.uditagarwal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class Seat {

    private final String seatId;
    private final int rowNo;
    private final int seatNo;
}
