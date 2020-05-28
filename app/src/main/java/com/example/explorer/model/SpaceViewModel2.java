package com.example.explorer.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.example.explorer.model.spaceResponse.Item;

import java.util.List;

public class SpaceViewModel2 extends AndroidViewModel {

    private MutableLiveData<List<Item>> mItemList;

    public SpaceViewModel2(@NonNull Application application) {
        super(application);
        mItemList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Item>> getItemList() {
        return mItemList;
    }

    public void setItemList(List<Item> itemList) {
        this.mItemList.setValue(itemList);
    }
}
