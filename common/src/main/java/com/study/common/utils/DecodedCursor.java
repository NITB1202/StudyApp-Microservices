package com.study.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class DecodedCursor {
    private LocalDate date;
    private UUID id;
}
