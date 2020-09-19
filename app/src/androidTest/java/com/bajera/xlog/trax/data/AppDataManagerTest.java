package com.bajera.xlog.trax.data;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.bajera.xlog.trax.data.db.AppDatabase;
import com.bajera.xlog.trax.data.db.model.Item;
import com.bajera.xlog.trax.data.db.model.Record;
import com.bajera.xlog.trax.data.db.model.ItemDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class AppDataManagerTest {
    private AppDataManager dm;
    private LocalDate sampleDate = LocalDate.of(1990, 1, 1);
    Item sampleItem = new Item("Name", Item.TrackingType.NUMBERS, Item.GoalType.TARGET, Item.GoalTimeFrame.WEEKLY, 20.0f);

    @Before
    public void createDb() throws NoSuchFieldException, IllegalAccessException {
        // Using reflection set AppDataManager.itemDao to a new and empty in memory room database.
        Context context = ApplicationProvider.getApplicationContext();
        dm = new AppDataManager(context);
        ItemDao dao = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build().itemDao();
        Field instanceField = AppDataManager.class.getDeclaredField("itemDao");
        instanceField.setAccessible(true);
        instanceField.set(dm, dao);
    }

    @Test
    public void getAllRecords() {
        dm.insertRecord(new Record(10.0f, sampleDate));
        dm.insertRecord(new Record(10.0f, sampleDate));
        dm.insertRecord(new Record(10.0f, sampleDate));
        assertThat(dm.getAllRecords(), hasSize(3));
    }

    @Test
    public void getRecordById() {
        long id = dm.insertRecord(new Record(10.0f, sampleDate));
        assertTrue(dm.getRecordById(id) != null);
    }

    @Test
    public void getRecordsBetweenDays() {
        dm.insertRecord(new Record(10.0f, sampleDate.minusYears(2)));
        dm.insertRecord(new Record(10.0f, sampleDate.minusYears(1)));
        dm.insertRecord(new Record(10.0f, sampleDate));
        dm.insertRecord(new Record(10.0f, sampleDate.plusYears(1)));
        dm.insertRecord(new Record(10.0f, sampleDate.plusYears(2)));
        List<Record> result = dm.getRecordsBetweenDates(sampleDate.minusYears(1), sampleDate.plusYears(1));
        assertThat(result, hasSize(3));
    }

    @Test
    public void getRecordsByItemId() {
        dm.insertRecord(new Record(10.0f, sampleDate, 1));
        dm.insertRecord(new Record(10.0f, sampleDate, 1));
        dm.insertRecord(new Record(10.0f, sampleDate, 2));
        assertThat(dm.getRecordsByItemId(1), hasSize(2));
    }

    @Test
    public void insertRecord() {
        dm.insertRecord(new Record(10.0f, sampleDate));
        assertThat(dm.getAllRecords(), hasSize(1));
    }

    @Test
    public void insertMultipleRecords() {
        dm.insertRecord(
            new Record(10.0f, sampleDate),
            new Record(10.0f, sampleDate),
            new Record(10.0f, sampleDate));
        assertThat(dm.getAllRecords(), hasSize(3));
    }

    @Test
    public void getAllItems() {
        dm.insertItem(sampleItem);
        dm.insertItem(sampleItem);
        dm.insertItem(sampleItem);
        assertThat(dm.getAllItems(), hasSize(3));
    }

    @Test
    public void getItemInfoById() {
        long id = dm.insertItem(sampleItem);
        assertTrue(dm.getItemById(id) != null);
    }

    @Test
    public void getItemByName() {
        String name = "A name";
        sampleItem.setName(name);
        dm.insertItem(sampleItem);
        assertTrue(dm.getItemByName(name).getName().equals(name));
    }

    @Test
    public void insertItem() {
        dm.insertItem(sampleItem);
        Item item = dm.getItemByName(sampleItem.getName());
        assertTrue(item.isSameAs(sampleItem));
    }

    @Test
    public void getRecordsByName() {
        String name = "some name";
        sampleItem.setName(name);
        long itemId = dm.insertItem(sampleItem);
        dm.insertRecord(
                new Record(10.0f, sampleDate, itemId),
                new Record(10.0f, sampleDate, itemId),
                new Record(10.0f, sampleDate, itemId));
        assertThat(dm.getRecordsByItemId(itemId), hasSize(3));
    }

    @Test
    public void recordsAreSortedByDateString() {
        dm.insertRecord(new Record(1.0f, LocalDate.of(1995, 1, 1))); // 4
        dm.insertRecord(new Record(1.0f, LocalDate.of(1990, 1, 1))); // 0
        dm.insertRecord(new Record(1.0f, LocalDate.of(1990, 2, 1))); // 2
        dm.insertRecord(new Record(1.0f, LocalDate.of(1990, 1, 20))); // 1
        dm.insertRecord(new Record(1.0f, LocalDate.of(1991, 1, 1))); // 3
        List<Record> records = dm.getAllRecords();
        LocalDate date = records.get(0).getDate();
        assertTrue(date.getYear() == 1990 && date.getMonthValue() == 1 && date.getDayOfMonth() == 1);
        date = records.get(1).getDate();
        assertTrue(date.getYear() == 1990 && date.getMonthValue() == 1 && date.getDayOfMonth() == 20);
        date = records.get(2).getDate();
        assertTrue(date.getYear() == 1990 && date.getMonthValue() == 2 && date.getDayOfMonth() == 1);
        date = records.get(3).getDate();
        assertTrue(date.getYear() == 1991 && date.getMonthValue() == 1 && date.getDayOfMonth() == 1);
        date = records.get(4).getDate();
        assertTrue(date.getYear() == 1995 && date.getMonthValue() == 1 && date.getDayOfMonth() == 1);
    }

    @Test
    public void deleteRecord() {
        Record record = new Record(0.0f, LocalDate.now(), 0);
        long id = dm.insertRecord(record);
        dm.deleteRecord(dm.getRecordById(id));
        assertEquals(dm.getAllRecords().size(), 0);
    }

    @Test
    public void updateRecord() {
        long id = dm.insertRecord(new Record(1.0f, LocalDate.now(), 0));
        Record record = dm.getRecordById(id);
        record.setValue(5.0f);
        dm.updateRecord(record);
        assertTrue(dm.getRecordById(id).getValue() == 5.0f);
    }


}