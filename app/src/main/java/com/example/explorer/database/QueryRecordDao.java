package com.example.explorer.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QueryRecordDao {

    @Query("Select * from searchRecord order by created_at asc")
    LiveData<List<QueryRecordEntry>> loadAllQueries();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertQuery(QueryRecordEntry record);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateQuery(QueryRecordEntry record);

    @Delete
    void deleteQueries(List<QueryRecordEntry> records);
}
