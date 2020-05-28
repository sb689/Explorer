package com.example.explorer.network;

import android.util.Log;

import com.example.explorer.model.imageResponse.ImageLinkObj;
import com.example.explorer.model.imageResponse.ImageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetImageData {

    private static final String TAG = GetImageData.class.getSimpleName();
    private static final String BASE_URL = "https://images-api.nasa.gov/";

    private static List<ImageLinkObj> mImageLinks;

    public static List<ImageLinkObj> getImageLinks(String assetId){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceApiService service = retrofit.create(SpaceApiService.class);
        Call<ImageResponse> call = service.getImageResult(assetId);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {

                Log.d(TAG, ":::::::::::::: call succeed, " + response.toString());
                Log.d(TAG, ":::::::::::::: call succeed, " + response.body());

                if (response.code() == 200) {
                    ImageResponse imageResponse = response.body();
                    List<ImageLinkObj> images = imageResponse.getCollection().getItems();
                    mImageLinks = images;
                    Log.d(TAG, ":::::::::::::: mImageLinks size = " + mImageLinks.size());

                } else {
                    mImageLinks = null;
                    Log.d(TAG, ":::::::::::::: call succeed with an error, " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.d(TAG, ":::::::::::::: retrofit call failed, ");
                Log.d(TAG, ":::::::::::::: retrofit call failed, " + t.getMessage());
                mImageLinks = null;
            }
        });

        return mImageLinks;
    }
}
