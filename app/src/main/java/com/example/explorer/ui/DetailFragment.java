package com.example.explorer.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.explorer.R;
import com.example.explorer.databinding.FragmentDetailBinding;
import com.example.explorer.model.SpaceViewModel2;
import com.example.explorer.model.spaceResponse.Item;
import com.example.explorer.model.spaceResponse.SpaceResponse;
import com.example.explorer.network.NetworkUtils;
import com.example.explorer.network.SpaceApiService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final String BUNDLE_POSITION_KEY = "item_position";
    private static final String BUNDLE_ASSET_ID_KEY = "load-asset";

    private  FragmentDetailBinding mDataBinding;
    public  List<Item> mItemList;
    public int mPosition;
    private boolean mAssetDetailView;


    public static DetailFragment newInstance(int position){
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_POSITION_KEY, position);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailFragment newInstance(String assetId){
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_ASSET_ID_KEY, assetId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( getArguments().containsKey(BUNDLE_ASSET_ID_KEY)) {
            return;
        }

        if (savedInstanceState != null) {

            mPosition = savedInstanceState.getInt(getString(R.string.saved_instance_bundle_key));
            Log.d(TAG, "onCreate, mPosition read from savedInstanceState, mPosition= " + mPosition );
        } else if (getArguments().containsKey(BUNDLE_POSITION_KEY)) {
            mPosition = getArguments().getInt(BUNDLE_POSITION_KEY);
            Log.d(TAG, "onCreate, mPosition read from arguments, mPosition= " + mPosition );
        }

    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false );

        if(getArguments().containsKey(BUNDLE_ASSET_ID_KEY)) {
            String assetId = getArguments().getString(BUNDLE_ASSET_ID_KEY);
            //get data for provided assetId
            mAssetDetailView = true;
            Log.d(TAG, ":::::::::::::  calling getAssetDetail");
            getAssetDetail(assetId);
        }
        else {

            SpaceViewModel2 viewModel = new ViewModelProvider(requireActivity()).get(SpaceViewModel2.class);
            viewModel.getItemList().observe(requireActivity(), new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> items) {
                    mItemList = items;
                    viewModel.getItemList().removeObservers(requireActivity());

                    //test block
                    Log.d(TAG, " mItemList status= " + mItemList);
                    if (mItemList != null) {
                        Log.d(TAG, " mItemList size= " + mItemList.size());
                    }
                    //test block ends

                    if (mItemList != null && mItemList.size() > 0) {
                        mAssetDetailView = false;
                        loadDetailUI(mPosition);
                    }
                }
            });

        }
        mDataBinding.ivFrontArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextItem();
            }
        });

        mDataBinding.ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrevItem();
            }
        });

        return mDataBinding.getRoot();
    }


    public void loadDetailUI(int position) {

        Log.d(TAG, ":::::::: inside loadDetailUI, position = "+ position);
        Item selectedItem = mItemList.get(position);
        mPosition = position;
        Log.d(TAG, "printing item detail to solve bug");
        Log.d(TAG, "title " + selectedItem.getData().get(0).getTitle());
        Log.d(TAG, "href: " + selectedItem.getLinks().get(0).getHref());

        mDataBinding.tvDetailTitle.setText(selectedItem.getData().get(0).getTitle());

        Log.d(TAG, ":::::::: inside loadDetailUI, title = "+ selectedItem.getData().get(0).getTitle());
        String imagePath = selectedItem.getLinks().get(0).getHref();
        Picasso.get().load(imagePath).into(mDataBinding.ivDetailImage);

        String creator = selectedItem.getData().get(0).getSecondary_creator();
        if(creator == null || creator.isEmpty()){
            creator = getContext().getString(R.string.secondary_creator_na);

        }
        mDataBinding.tvCreator.setText(creator);
        mDataBinding.tvDate.setText(selectedItem.getData().get(0).getDate_created());
        mDataBinding.tvDescription.setText(selectedItem.getData().get(0).getDescription());

    }


    private  void loadDetailAssetInfo(Item item) {


        mDataBinding.tvDetailTitle.setText(item.getData().get(0).getTitle());

        String imagePath = item.getLinks().get(0).getHref();
        Picasso.get().load(imagePath).into(mDataBinding.ivDetailImage);

        String creator = item.getData().get(0).getSecondary_creator();
        if(creator == null || creator.isEmpty()){
            creator = getContext().getString(R.string.secondary_creator_na);
        }
        mDataBinding.tvCreator.setText(creator);
        mDataBinding.tvDate.setText(item.getData().get(0).getDate_created());
        mDataBinding.tvDescription.setText(item.getData().get(0).getDescription());

    }

    public void showNextItem(){
        Log.d(TAG, ":::::::: from nextCLicked to showNextItem");
        int position = mPosition;

        if(position + 1 > mItemList.size()-1){
            position = 0;
        }else{
            position = position + 1;
        }
        loadDetailUI(position);
    }

    public void showPrevItem(){

        Log.d(TAG, ":::::::: from backClicked to showPrevItem");
        int position = mPosition;
        if(position - 1 < 0){
            position = mItemList.size() -1;
        }
        else{
            position = position -1;
        }

        Log.d(TAG, ":::::::: showPrevItem, position = "+ position);
        loadDetailUI(position);
    }


    private void showErrorMessage(){
        mDataBinding.tvDetailErrorMsg.setText(getString(R.string.main_error_msg_no_data_found));
        mDataBinding.tvDetailErrorMsg.setVisibility(View.VISIBLE);
        mDataBinding.pbDetail.setVisibility(View.GONE);
    }


    private void loadDetailsForAsset(Item item){

         mDataBinding.pbDetail.setVisibility(View.GONE);
        loadDetailAssetInfo(item);

    }


    public void getAssetDetail(String assetId){

        if(!NetworkUtils.isNetworkConnected(getActivity())){
            showErrorMessage();
            return;
        }

        Log.d(TAG, "getAssetDetail, assetID = "+ assetId);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SpaceApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceApiService service = retrofit.create(SpaceApiService.class);

        mDataBinding.pbDetail.setVisibility(View.VISIBLE);
        Call<SpaceResponse> call = service.getAsset(assetId, SpaceApiService.PARAM_MEDIA_TYPE);
        call.enqueue(new Callback<SpaceResponse>() {

            @Override
            public void onResponse(Call<SpaceResponse> call, Response<SpaceResponse> response) {
                Log.d(TAG, ":::::::::::::: call succeed, " + response.toString());
                Log.d(TAG, ":::::::::::::: call succeed, " + response.body());


                if (response.code() == 200) {
                    SpaceResponse spaceResponse = response.body();
                    List<Item> items = spaceResponse.getCollection().getItems();

                    if(items != null && items.size() > 0){

                        Log.d(TAG, ":::::::::::::: inside onResponse, items.size = " + items.size());
                        loadDetailsForAsset(items.get(0));
                    }
                    else{
                        Log.d(TAG, ":::::::::::::: call succeed, no data found " + response.toString());
                      showErrorMessage();
                }
                } else {

                    Log.d(TAG, ":::::::::::::: call succeed with an error, " + response.toString());
                   showErrorMessage();
                }

            }

            @Override
            public void onFailure(Call<SpaceResponse> call, Throwable t) {
                Log.d(TAG, ":::::::::::::: retrofit call failed, ");
                Log.d(TAG, ":::::::::::::: retrofit call failed, " + t.getMessage());
               showErrorMessage();
            }

        });


    }


    private void hideNavigationButtons()
    {
        mDataBinding.ivBackArrow.setVisibility(View.GONE);
        mDataBinding.ivFrontArrow.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        if(mAssetDetailView) {
            hideNavigationButtons();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.saved_instance_bundle_key), mPosition);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mItemList = null;
        mDataBinding = null;
    }


}
