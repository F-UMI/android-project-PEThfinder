package com.example.pethfinder;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

public class LocalDateTimeConverter {
    @TypeConverter
    public static LocalDateTime toDate(Long timestamp) {
        LocalDateTime ldt;
        if (timestamp == null){
            return null;
        }else{
            ldt = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return ldt;
    }

    @TypeConverter
    public static Long toTimestamp(LocalDateTime date) {
        if (date == null){
            return  null;
        }else {
            return date.getLong(ChronoField.CLOCK_HOUR_OF_DAY);
        }
    }}