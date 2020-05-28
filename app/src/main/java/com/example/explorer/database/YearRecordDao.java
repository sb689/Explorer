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
public interface YearRecordDao {

    @Query("Select * from yearRecord order by created_at asc")
    LiveData<List<YearRecordEntry>> loadAllYears();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertYear(YearRecordEntry record);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateYear(YearRecordEntry record);

    @Delete
    void deleteYears(List<YearRecordEntry> records);
}
