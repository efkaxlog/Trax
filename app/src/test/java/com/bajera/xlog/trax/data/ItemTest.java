package com.bajera.xlog.trax.data;

import com.bajera.xlog.trax.data.db.model.Item;
import com.bajera.xlog.trax.data.db.model.Record;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ItemTest {

    @Test
    public void isSameAs() {
        Item item = new Item("name", Item.TrackingType.NUMBERS, Item.GoalType.NONE,
                Item.GoalTimeFrame.NONE, 0.0f);
        Item other = new Item("name", Item.TrackingType.NUMBERS, Item.GoalType.NONE,
                Item.GoalTimeFrame.NONE, 0.0f);
        assertTrue(item.isSameAs(other));
    }

    @Test
    public void getGoalProgressPercent() {
        Item item = new Item("name", Item.TrackingType.NUMBERS, Item.GoalType.TARGET,
                Item.GoalTimeFrame.DAILY, 100.0f);
        assertTrue(20.0f == item.getGoalProgressPercent(20.0f));
    }

    @Test
    public void getGoalProgressLabel() {
        Item item = new Item("name", Item.TrackingType.NUMBERS, Item.GoalType.TARGET,
                Item.GoalTimeFrame.DAILY, 100.0f);
        String expected = "20.0/100.0";
        assertEquals(expected, item.getGoalProgressLabel(20.0f));
    }
}