package com.example.explorer.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.explorer.R;
import com.example.explorer.SpaceItemListAdapter;
import com.example.explorer.databinding.FragmentListBinding;
import com.example.explorer.model.SpaceViewModel2;
import com.example.explorer.model.spaceResponse.Item;

import java.util.List;

public class ListFragment extends Fragment implements SpaceItemListAdapter.itemClickedListener {

    private static final String TAG = ListFragment.class.getSimpleName();
    private FragmentListBinding mDataBinding;
    private SpaceItemListAdapter mAdapter;

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

        SpaceViewModel2 viewModel = new ViewModelProvider(requireActivity()).get(SpaceViewModel2.class);
        viewModel.getItemList().observe(getActivity(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                mAdapter.setSpaceData(items);
            }
        });


        return mDataBinding.getRoot();
    }



    @Override
    public void itemClicked(int position) {
        Log.d(TAG, "item clicked received ListFragment");
        ((SearchActivity) requireActivity()).showDetail(position);
    }
}
