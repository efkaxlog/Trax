package com.bajera.xlog.trax.data;

import android.content.Context;

import com.bajera.xlog.trax.data.db.AppDatabase;
import com.bajera.xlog.trax.data.db.model.Item;
import com.bajera.xlog.trax.data.db.model.ItemDao;
import com.bajera.xlog.trax.data.db.model.Record;

import java.time.LocalDate;
import java.util.List;

public class AppDataManager implements ItemDao, ResourceManager {
    private ItemDao itemDao;
    private ResourceManager resourceManager;

    public AppDataManager(Context context) {
        itemDao = AppDatabase.getInstance(context).itemDao();
        resourceManager = new AndroidResourceManager(context);
    }

    public List<Record> getAllRecords() {
        return itemDao.getAllRecords();
    }

    @Override
    public Record getRecordById(long id) {
        return itemDao.getRecordById(id);
    }

    @Override
    public List<Record> getRecordsBetweenDates(LocalDate from, LocalDate to) {
        return itemDao.getRecordsBetweenDates(from, to);
    }

    @Override
    public List<Record> getRecordsBetweenDates(long itemId, LocalDate from, LocalDate to) {
        return itemDao.getRecordsBetweenDates(itemId, from, to);
    }

    @Override
    public List<Record> getRecordsByDate(long itemId, LocalDate date) {
        return itemDao.getRecordsByDate(itemId, date);
    }

    @Override
    public List<Record> getRecordsByItemId(long itemId) {
        return itemDao.getRecordsByItemId(itemId);
    }

    @Override
    public Long insertRecord(Record record) {
        return itemDao.insertRecord(record);
    }

    @Override
    public List<Long> insertRecord(Record... records) {
        return itemDao.insertRecord(records);
    }

    @Override
    public void deleteRecord(Record record) {
        itemDao.deleteRecord(record);
    }

    @Override
    public void updateRecord(Record record) {
        itemDao.updateRecord(record);
    }

    @Override
    public List<Item> getAllItems() {
        return itemDao.getAllItems();
    }

    @Override
    public Item getItemById(long id) {
        return itemDao.getItemById(id);
    }

    @Override
    public Item getItemByName(String name) {
        return itemDao.getItemByName(name);
    }

    @Override
    public Long insertItem(Item item) {
        return itemDao.insertItem(item);
    }

    @Override
    public void deleteItem(Item item) {
        itemDao.deleteItem(item);
    }

    @Override
    public void updateItem(Item item) {
        itemDao.updateItem(item);
    }


    @Override
    public int getCalendarNumbersDrawableId() {
        return resourceManager.getCalendarNumbersDrawableId();
    }

    @Override
    public int getCalendarDailyTaskDrawableId() {
        return resourceManager.getCalendarDailyTaskDrawableId();
    }

    @Override
    public int getChartDatasetColour() {
        return resourceManager.getChartDatasetColour();
    }

    @Override
    public int getSecondaryColour() {
        return resourceManager.getSecondaryColour();
    }
}