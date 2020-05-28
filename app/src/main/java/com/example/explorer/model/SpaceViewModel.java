package com.example.explorer.model;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.explorer.model.spaceResponse.Item;
import com.example.explorer.model.spaceResponse.SpaceResponse;
import com.example.explorer.network.SpaceApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpaceViewModel extends ViewModel {

    private static final String TAG = SpaceViewModel.class.getSimpleName();
    private static final String BASE_URL = "https://images-api.nasa.gov/";
    private static final String PARAM_MEDIA_TYPE = "image";

    private MutableLiveData <List<Item>> mData;
    private String mQuery;
    private String mYearStart;
    private String mYearEnd;

    public SpaceViewModel(String query, String startYear, String endYear) {
        mQuery = query;
        mYearStart = startYear;
        mYearEnd = endYear;
    }

    public SpaceViewModel() {
        mData = new MutableLiveData<>();
    }

    public void setSearchData(List<Item> items){
        mData.setValue(items);
    }

    public MutableLiveData<List<Item>> getItemList() {
        if(mData == null){
            mData = new MutableLiveData<>();
            getSpaceData();
        }
        return mData;
    }



    public void getSpaceData(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceApiService service = retrofit.create(SpaceApiService.class);
        Call<SpaceResponse> call = service.getResult(mQuery, mYearStart, mYearEnd, PARAM_MEDIA_TYPE);

        call.enqueue(new Callback<SpaceResponse>() {
            @Override
            public void onResponse(Call<SpaceResponse> call, Response<SpaceResponse> response) {
                Log.d(TAG, ":::::::::::::: call succeed, " + response.toString());
                Log.d(TAG, ":::::::::::::: call succeed, " + response.body());

                if (response.code() == 200) {
                    SpaceResponse spaceResponse = response.body();
                    List<Item> items = spaceResponse.getCollection().getItems();
                    Log.d(TAG, ":::::::::::::: inside onResponse, items.size = " + items.size());
                    if(items.size() == 0){
                        mData.setValue(null);
                    }else {
                        mData.setValue(items);
                    }
                } else {
                    mData.setValue(null);
                    Log.d(TAG, ":::::::::::::: call succeed with an error, " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<SpaceResponse> call, Throwable t) {
                Log.d(TAG, ":::::::::::::: retrofit call failed, ");
                Log.d(TAG, ":::::::::::::: retrofit call failed, " + t.getMessage());
                mData.setValue(null);
            }
        });
    }
}


