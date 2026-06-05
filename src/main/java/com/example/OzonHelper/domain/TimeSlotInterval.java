package com.example.OzonHelper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TimeSlotInterval {
    private LocalDateTime from;
    private LocalDateTime to;
}
