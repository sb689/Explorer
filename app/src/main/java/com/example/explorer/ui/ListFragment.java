package com.example.explorer.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.explorer.R;
import com.example.explorer.databinding.FragmentListBinding;
import com.example.explorer.model.SpaceViewModel2;
import com.example.explorer.model.spaceResponse.Item;
import com.google.firebase.analytics.FirebaseAnalytics;


import java.util.List;

public class ListFragment extends Fragment implements SpaceItemListAdapter.itemClickedListener {

    public static final String TAG = ListFragment.class.getSimpleName();
    private FragmentListBinding mDataBinding;
    private SpaceItemListAdapter mAdapter;

    public static ListFragment newInstance()
    {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        mAdapter = new SpaceItemListAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mDataBinding.rvSpaceList.setLayoutManager(layoutManager);
        mDataBinding.rvSpaceList.setAdapter(mAdapter);
        mDataBinding.rvSpaceList.setHasFixedSize(true);
        mDataBinding.rvSpaceList.setPadding(0,0,0, SearchActivity.mAdViewHeight + 30);


        SpaceViewModel2 viewModel = new ViewModelProvider(requireActivity()).get(SpaceViewModel2.class);
        viewModel.getItemList().observe(requireActivity(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                mAdapter.setSpaceData(items);
                viewModel.getItemList().removeObservers(requireActivity());
            }
        });

        return mDataBinding.getRoot();
    }



    @Override
    public void itemClicked(int position) {

        //log events analytics
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.analytics_selected_response));
        bundle.putString(FirebaseAnalytics.Param.VALUE, Integer.toString(position));
        SearchActivity.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle);

        ((SearchActivity)requireActivity()).itemSelectedForDetailView(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.setSpaceData(null);
        mAdapter = null;
        mDataBinding.rvSpaceList.setAdapter(null);
        mDataBinding = null;

    }


}
