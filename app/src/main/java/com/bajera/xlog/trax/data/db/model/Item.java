package com.bajera.xlog.trax.data.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bajera.xlog.trax.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * Class used for storing an Item's information. It will be stored in a table as one row.
 */
@Entity(tableName = "items")
public class Item {

    public enum TrackingType {NUMBERS, ONCE_A_DAY}

    public enum GoalType {NONE, TARGET, LIMIT}

    public enum GoalTimeFrame {DAILY, WEEKLY, MONTHLY}

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "tracking_type")
    private TrackingType trackingType;

    @ColumnInfo(name = "goal_type")
    private GoalType goalType;

    @ColumnInfo(name = "goal_time")
    private GoalTimeFrame goalTimeFrame;

    @ColumnInfo(name = "goal_value")
    private float goalValue;

    @Ignore
    public Item() {
        name = "";
        trackingType = TrackingType.NUMBERS;
        goalType = GoalType.NONE;
        goalTimeFrame = GoalTimeFrame.DAILY;
        goalValue = 0.0f;
    }

    public Item(String name, TrackingType trackingType, GoalType goalType, GoalTimeFrame goalTimeFrame, float goalValue) {
        this.name = name;
        this.trackingType = trackingType;
        this.goalType = goalType;
        this.goalTimeFrame = goalTimeFrame;
        this.goalValue = goalValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrackingType getTrackingType() {
        return trackingType;
    }

    public void setTrackingType(TrackingType trackingType) {
        this.trackingType = trackingType;
    }

    public void setTrackingType(int index) {
        this.trackingType = TrackingType.values()[index];
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }

    public void setGoalType(int index) {
        this.goalType = GoalType.values()[index];
    }


    public GoalTimeFrame getGoalTimeFrame() {
        return goalTimeFrame;
    }

    public void setGoalTimeFrame(GoalTimeFrame goalTimeFrame) {
        this.goalTimeFrame = goalTimeFrame;
    }

    public void setGoalTimeFrame(int index) {
        this.goalTimeFrame = GoalTimeFrame.values()[index];
    }


    public float getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(float goalValue) {
        this.goalValue = goalValue;
    }

    public void setGoalValue(String goalValue) {
        try {
            this.goalValue = Float.parseFloat(goalValue);
        } catch (Exception e) {
            this.goalValue = 0.0f;
        }
    }

    /**
     * @return Formatted String of e.g. "Target to 100 every week".
     */
    public String getGoalSummary() {
        StringBuilder sb = new StringBuilder();
        if (goalType == GoalType.LIMIT) {
            sb.append("Limit to ");
        } else if (goalType == GoalType.TARGET) {
            sb.append("Target of ");
        }
        sb.append(StringUtils.format(goalValue) + " every ");
        if (goalTimeFrame == GoalTimeFrame.DAILY) {
            sb.append("day");
        } else if (goalTimeFrame == GoalTimeFrame.WEEKLY) {
            sb.append("week");
        } else if (goalTimeFrame == GoalTimeFrame.MONTHLY) {
            sb.append("month");
        }
        return sb.toString();
    }

    public boolean isSameAs(Item item) {
        return name.equals(item.getName()) && trackingType == item.getTrackingType() &&
                goalType == item.getGoalType() && goalTimeFrame == item.getGoalTimeFrame() &&
                goalValue == item.getGoalValue();
    }

    /**
     * @param records List of records from where each of their values will be accumulated to calculate
     *                goal progress value. The list should be made up of records from days matching the
     *                goal time frame e.g. if time frame is weekly, records should be from monday of
     *                this week, if monthly, from first day of the month.
     * @return Goal progress value.
     */
    public float getGoalProgress(List<Record> records) {
        float value = 0.0f;
        for (Record r : records) {
            value += r.getValue();
        }
        return value;
    }

    /**
     * Calculates goal progress percentage. If limit is the goal type, the percentage represents
     * how much of the limit is already used. Can return a value of more than 100, meaning over
     * target or limit.
     *
     * @return Goal progress percentage.
     */
    public int getGoalProgressPercent(float progressValue) {
        return (int) (progressValue / goalValue * 100);
    }

    /**
     * @return Progress of goal String object e.g. "34.4/70.0"
     */
    public String getGoalProgressLabel(float goalProgress) {
        return StringUtils.format(goalProgress) + "/" + StringUtils.format(goalValue);
    }

    public String getGoalDaysLeftLabel() {
        LocalDate goalEndDate;
        LocalDate now = LocalDate.now();
        if (goalTimeFrame == GoalTimeFrame.DAILY) {
            goalEndDate = now.plusDays(1);
        } else if (goalTimeFrame == GoalTimeFrame.WEEKLY) {
            goalEndDate = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        } else if (goalTimeFrame == GoalTimeFrame.MONTHLY) {
            goalEndDate = now.with(TemporalAdjusters.firstDayOfNextMonth());
        } else {
            return "Error";
        }
        return Integer.toString(Period.between(now, goalEndDate).getDays());
    }

    public String goalTypeToString() {
        if (goalType == GoalType.TARGET) {
            return "Target";
        } else if (goalType == GoalType.LIMIT) {
            return "Limit";
        } else {
            return "No goal";
        }
    }
}
