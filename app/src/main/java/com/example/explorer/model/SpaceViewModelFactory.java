package com.example.explorer.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SpaceViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String mQuery;
    private String mStartYear;
    private String mEndYear;

    public SpaceViewModelFactory(String mQuery, String mStartYear, String mEndYear) {
        this.mQuery = mQuery;
        this.mStartYear = mStartYear;
        this.mEndYear = mEndYear;
    }

    // COMPLETED (4) Uncomment the following method
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SpaceViewModel(mQuery, mStartYear, mEndYear);
    }
}
