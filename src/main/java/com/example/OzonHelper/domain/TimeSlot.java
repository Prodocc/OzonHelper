package com.example.OzonHelper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TimeSlot {
    private LocalDate dateInTimeZone;
    private List<TimeSlotInterval> intervals = new ArrayList<>();
}
