package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.TimeSlot;
import com.example.OzonHelper.domain.TimeSlotInterval;
import com.example.OzonHelper.dto.response.fbo.SupplyTimeSlotInfoDto;
import com.example.OzonHelper.dto.response.fbo.TimeSlotDto;
import com.example.OzonHelper.dto.response.fbo.WarehouseTimeSlotByDaysDto;
import com.example.OzonHelper.dto.response.fbo.WarehouseTimeSlotsDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupplyOrderMapper {

    public TimeSlot mapToModel(WarehouseTimeSlotByDaysDto timeSlotsDtoByDays) {
        TimeSlot timeSlot = new TimeSlot();

        timeSlot.setDateInTimeZone(LocalDate.parse(timeSlotsDtoByDays.getDateInTimezone()));
        for (WarehouseTimeSlotByDaysDto.TimeSlotDto dto : timeSlotsDtoByDays.getTimeSlots()) {
            LocalDateTime from = LocalDateTime.parse(dto.getFrom());
            LocalDateTime to = LocalDateTime.parse(dto.getTo());

            TimeSlotInterval interval = new TimeSlotInterval(from, to);
            timeSlot.getIntervals().add(interval);
        }

        return timeSlot;
    }
}
