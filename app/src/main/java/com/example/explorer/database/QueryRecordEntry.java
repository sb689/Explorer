package com.example.explorer.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "searchRecord", indices = {@Index(value = {"search_key"},
        unique = true)})
public class QueryRecordEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "search_key")
    private String searchKey;
    @ColumnInfo(name = "created_at")
    private long createdAt;



    @Ignore
    public QueryRecordEntry(String searchKey, long createdAt) {
        this.searchKey = searchKey;
        this.createdAt = createdAt;
    }

    public QueryRecordEntry(int id, String searchKey, long createdAt) {
        this.id = id;
        this.searchKey = searchKey;
        this.createdAt = createdAt;
    }


    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }


}
