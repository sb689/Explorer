package com.example.explorer.model;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;


import com.example.explorer.model.spaceResponse.Item;
import com.example.explorer.model.spaceResponse.SpaceResponse;
import com.example.explorer.network.NetworkUtils;
import com.example.explorer.network.SpaceApiService;
import com.example.explorer.ui.SearchActivity;

import java.sql.PreparedStatement;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpaceViewModel2 extends AndroidViewModel {

    private static final String TAG = SpaceViewModel2.class.getSimpleName();

    private  MutableLiveData<List<Item>> mItemList;

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
