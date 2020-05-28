package com.example.explorer.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "yearRecord", indices = {@Index(value = {"year"},
        unique = true)})

public class YearRecordEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String year;
    @ColumnInfo(name = "created_at")
    private long createdAt;

    public YearRecordEntry(int id, String year, long createdAt) {
        this.id = id;
        this.year = year;
        this.createdAt = createdAt;
    }

    @Ignore
    public YearRecordEntry(String year, long createdAt) {
        this.year = year;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
