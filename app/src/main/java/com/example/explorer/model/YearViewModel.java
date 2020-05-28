package com.example.explorer.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.explorer.database.AppDatabase;
import com.example.explorer.database.YearRecordEntry;

import java.util.List;

public class YearViewModel extends AndroidViewModel {

    private LiveData<List<YearRecordEntry>> mRecords;

    public YearViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        mRecords = db.yearRecordDao().loadAllYears();
    }

    public LiveData<List<YearRecordEntry>> getRecords() {
        return mRecords;
    }


}
