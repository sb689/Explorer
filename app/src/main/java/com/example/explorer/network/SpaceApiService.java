package com.example.explorer.network;

import com.example.explorer.model.imageResponse.ImageResponse;
import com.example.explorer.model.spaceResponse.SpaceResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpaceApiService {

    public static final String BASE_URL = "https://images-api.nasa.gov/";
    public static final String PARAM_MEDIA_TYPE = "image";

    @GET("/search")
    Call<SpaceResponse> getResult(@Query("q") String query, @Query("year_start") String yearStart, @Query("year_end") String yearEnd,
                                  @Query("media_type") String mediaType);

    @GET("/search")
    Call<SpaceResponse> getAsset(@Query("nasa_id") String assetId,
                                  @Query("media_type") String mediaType);


    @GET("asset/{assetId}")
    Call<ImageResponse> getImageResult(@Path("assetId") String assetId);

}
