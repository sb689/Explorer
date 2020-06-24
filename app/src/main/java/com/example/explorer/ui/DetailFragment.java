package com.example.explorer.ui;

import android.content.Context;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.explorer.R;

import com.example.explorer.databinding.FragmentDetailBinding;
import com.example.explorer.model.SpaceViewModel2;
import com.example.explorer.model.spaceResponse.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final String BUNDLE_POSITION_KEY = "item_position";

    private static FragmentDetailBinding mDataBinding;
    private static Item mSelectedItem;
    public static List<Item> mItemList;
    public static int mPosition;
    private static Context mContext;


    public static DetailFragment newInstance(int position){
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_POSITION_KEY, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpaceViewModel2 viewModel = new ViewModelProvider(requireActivity()).get(SpaceViewModel2.class);
        viewModel.getItemList().observe(getActivity(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                viewModel.getItemList().removeObserver(this);
                mItemList = items;
            }
        });

        if(getArguments().containsKey(BUNDLE_POSITION_KEY)){
            mPosition = getArguments().getInt(BUNDLE_POSITION_KEY);
        }

        if(savedInstanceState == null){
            mPosition = SearchActivity.getmPosition();
        }else{
            mPosition = savedInstanceState.getInt(getString(R.string.saved_instance_bundle_key));
        }
        mSelectedItem = mItemList.get(mPosition);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false );
        mContext = getActivity();


        loadDetailUI(mPosition);

        return mDataBinding.getRoot();
    }


    public static void loadDetailUI(int position) {

        mSelectedItem = mItemList.get(position);
        mPosition = position;
        mDataBinding.tvDetailTitle.setText(mSelectedItem.getData().get(0).getTitle());

        String imagePath = mSelectedItem.getLinks().get(0).getHref();
        Picasso.get().load(imagePath).into(mDataBinding.ivDetailImage);

        String creator = mSelectedItem.getData().get(0).getSecondary_creator();
        if(creator == null || creator.isEmpty()){
            creator = mContext.getString(R.string.secondary_creator_na);
        }
        
        mDataBinding.tvCreator.setText(creator);
        mDataBinding.tvDate.setText(mSelectedItem.getData().get(0).getDate_created());
        mDataBinding.tvDescription.setText(mSelectedItem.getData().get(0).getDescription());


    }

    public void showNextItem(){

        int position = mPosition;

        if(position + 1 > mItemList.size()-1){
            position = 0;
        }else{
            position = position + 1;
        }

        loadDetailUI(position);
    }

    public void showPrevItem(){

        int position = mPosition;

        if(position - 1 < 0){
            position = mItemList.size() -1;
        }
        else{
            position = position -1;
        }

        loadDetailUI(position);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        ((SearchActivity) requireActivity()).showNavigationButtons();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((SearchActivity) requireActivity()).hideNavigationButtons();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.saved_instance_bundle_key), mPosition);
    }
}
