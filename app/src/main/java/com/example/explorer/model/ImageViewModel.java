package com.example.explorer.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.explorer.model.imageResponse.ImageLinkObj;
import com.example.explorer.model.imageResponse.ImageResponse;
import com.example.explorer.model.spaceResponse.Item;
import com.example.explorer.network.GetImageData;
import com.example.explorer.network.SpaceApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageViewModel extends ViewModel {

    private static final String TAG = GetImageData.class.getSimpleName();
    private static final String BASE_URL = "https://images-api.nasa.gov/";

    private static MutableLiveData< List<ImageLinkObj>> mImageLinks;
    private String assetId;

    public ImageViewModel(String assetId) {

        this.assetId = assetId;

    }

    public MutableLiveData< List<ImageLinkObj>> getImages() {

        mImageLinks = new MutableLiveData<>();
        getImageLinks(assetId);
        return mImageLinks;
    }

    public static void getImageLinks(String assetId){

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
                    mImageLinks.setValue(images);

                } else {
                    mImageLinks.setValue(null);
                    Log.d(TAG, ":::::::::::::: call succeed with an error, " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.d(TAG, ":::::::::::::: retrofit call failed, ");
                Log.d(TAG, ":::::::::::::: retrofit call failed, " + t.getMessage());
                mImageLinks.setValue(null);
            }
        });
    }
}
