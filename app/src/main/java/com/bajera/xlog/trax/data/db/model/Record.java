package com.bajera.xlog.trax.data.db.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "records")
public class Record implements Comparable<Record> {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    @ColumnInfo(name = "value")
    @NonNull
    private float value;

    @ColumnInfo(name = "date")
    @NonNull
    private LocalDate date;

    @ColumnInfo(name = "itemId")
    @NonNull
    private long itemId;

    @Ignore
    public Record(float value, LocalDate date) {
        this.value = value;
        this.date = date;
    }

    public Record(float value, LocalDate date, long itemId) {
        this.value = value;
        this.date = date;
        this.itemId = itemId;
    }

    public void addValue(float value) {
        this.value += value;
    }

    public boolean equalTo(Record other) {
        return (value == other.value && date.equals(other.date));
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", value=" + value +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(Record other) {
        if (date.isBefore(other.date)) {
            return -1;
        } else if (date.equals(other.date)) {
            return 0;
        } else {
            return 1;
        }
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}