package com.example.explorer.network;

import android.util.Log;


import com.example.explorer.model.SpaceViewModel;
import com.example.explorer.model.spaceResponse.Item;
import com.example.explorer.model.spaceResponse.SpaceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GetSearchData {

    private static final String TAG = SpaceViewModel.class.getSimpleName();
    private static final String BASE_URL = "https://images-api.nasa.gov/";
    private static final String PARAM_MEDIA_TYPE = "image";

    private String mQuery;
    private String mYearStart;
    private String mYearEnd;
    private List<Item> mResponseData;


    public GetSearchData(String mQuery, String mYearStart, String mYearEnd) {
        this.mQuery = mQuery;
        this.mYearStart = mYearStart;
        this.mYearEnd = mYearEnd;
    }

    public List<Item> getSpaceData(){

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
                    mResponseData = items;

                } else {
                    Log.d(TAG, ":::::::::::::: call succeed with an error, " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<SpaceResponse> call, Throwable t) {
                Log.d(TAG, ":::::::::::::: retrofit call failed, ");
                Log.d(TAG, ":::::::::::::: retrofit call failed, " + t.getMessage());

            }
        });
        return mResponseData;
    }

}
