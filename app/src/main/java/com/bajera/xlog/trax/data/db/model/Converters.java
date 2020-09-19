package com.bajera.xlog.trax.data.db.model;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class Converters {
    @TypeConverter
    public static LocalDate dateFromString(String dateString) {
        return dateString == null ? null : LocalDate.parse(dateString);
    }

    @TypeConverter
    public static String stringFromDate(LocalDate date) {
        return date == null ? null : date.toString();
    }

    @TypeConverter
    public static Item.TrackingType trackingTypeFromString(String trackingType) {
        return Item.TrackingType.valueOf(trackingType);
    }

    @TypeConverter
    public static String stringFromTrackingType(Item.TrackingType trackingType) {
        return trackingType.name();
    }

    @TypeConverter
    public static Item.GoalType goalTypeFromString(String goalType) {
        return Item.GoalType.valueOf(goalType);
    }

    @TypeConverter
    public static String stringFromGoalType(Item.GoalType goalType) {
        return goalType.name();
    }

    @TypeConverter
    public static Item.GoalTimeFrame goalTimeFrameFromString(String goalTimeFrame) {
        return Item.GoalTimeFrame.valueOf(goalTimeFrame);
    }

    @TypeConverter
    public static String stringFromGoalTimeFrame(Item.GoalTimeFrame goalTimeFrame) {
        return goalTimeFrame.name();
    }
}
