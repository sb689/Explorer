package com.example.explorer.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.explorer.model.spaceResponse.Item;

import java.util.List;

public class AssetViewModel extends AndroidViewModel {


    private static final String TAG = AssetViewModel.class.getSimpleName();
    private MutableLiveData<Item> mItem;

    public AssetViewModel(@NonNull Application application) {
        super(application);
        mItem = new MutableLiveData<>();
    }


    public MutableLiveData<Item> getmItem() {
        return mItem;
    }

    public void setmItem(Item item) {

        this.mItem.setValue(item);
    }
}
