package com.bajera.xlog.trax.data.db.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface ItemDao {
    // --- Record methods ---
    @Query("SELECT * FROM records ORDER BY date ASC")
    List<Record> getAllRecords();

    @Query("SELECT * FROM records WHERE id=:id")
    Record getRecordById(long id);

    @Query("SELECT * FROM records WHERE date BETWEEN :from AND :to ORDER BY date ASC")
    List<Record> getRecordsBetweenDates(LocalDate from, LocalDate to);

    @Query("SELECT * FROM records WHERE itemId=:itemId AND date BETWEEN :from AND :to ORDER BY date ASC")
    List<Record> getRecordsBetweenDates(long itemId, LocalDate from, LocalDate to);

    @Query("SELECT * FROM records WHERE itemId=:itemId AND date=:date ORDER BY date ASC")
    List<Record> getRecordsByDate(long itemId, LocalDate date);

    @Query("SELECT * FROM records WHERE itemId=:itemId ORDER BY date ASC")
    List<Record> getRecordsByItemId(long itemId);

    @Insert
    Long insertRecord(Record record);

    @Insert
    List<Long> insertRecord(Record... records);

    @Delete
    void deleteRecord(Record record);

    @Update
    void updateRecord(Record record);

    // --- Item methods ---
    @Query("SELECT * FROM items")
    List<Item> getAllItems();

    @Query("SELECT * FROM items WHERE id=:id")
    Item getItemById(long id);

    @Query("SELECT * FROM items WHERE name=:name")
    Item getItemByName(String name);

    @Insert
    Long insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Update
    void updateItem(Item item);
}