package com.example.explorer.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.explorer.database.AppDatabase;
import com.example.explorer.database.QueryRecordEntry;

import java.util.List;

public class QueryHistoryViewModel extends AndroidViewModel {

    private LiveData<List<QueryRecordEntry>> mRecords;

    public LiveData<List<QueryRecordEntry>> getRecords() {
        return mRecords;
    }

    public QueryHistoryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDb = AppDatabase.getInstance(getApplication());
        mRecords = appDb.searchRecordDao().loadAllQueries();
    }


}
