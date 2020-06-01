package com.example.explorer.ui;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explorer.databinding.SpaceItemBinding;

import com.example.explorer.model.spaceResponse.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SpaceItemListAdapter extends RecyclerView.Adapter<SpaceItemListAdapter.SpaceItemListViewHolder>{

    private static final String TAG = SpaceItemListAdapter.class.getSimpleName();
    private static List<Item> mItemList;
    private final itemClickedListener mItemClickedListener;

    public SpaceItemListAdapter( SpaceItemListAdapter.itemClickedListener itemClickedListener) {

        mItemClickedListener = itemClickedListener;
    }

    public void setSpaceData(List<Item> items){
        mItemList = items;
        notifyDataSetChanged();
    }

    public interface itemClickedListener{
        void itemClicked(int position);
    }

    @NonNull
    @Override
    public SpaceItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SpaceItemBinding itemBinding = SpaceItemBinding.inflate(layoutInflater, parent,false);
        return new SpaceItemListViewHolder( itemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull SpaceItemListViewHolder holder, int position) {

        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        if(mItemList == null)
            return 0;
        else
            return mItemList.size();
    }


    public class SpaceItemListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final SpaceItemBinding mDataBinding;

        SpaceItemListViewHolder(@NonNull SpaceItemBinding dataBinding) {
            super(dataBinding.getRoot());
            mDataBinding = dataBinding;
           mDataBinding.getRoot().setOnClickListener(this);
        }

        void bindData(int position){
            String imagePath = mItemList.get(position).getLinks().get(0).getHref();
            Picasso.get().load(imagePath).into(mDataBinding.ivSpaceImage);
            String imageTitle = mItemList.get(position).getData().get(0).getTitle();
            mDataBinding.tvTitle.setText(imageTitle);
            String shortDesc = mItemList.get(position).getData().get(0).getDescription();
            mDataBinding.tvShortDesc.setText(shortDesc);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "item clicked received in adapter" );
            int position = getAdapterPosition();
            mItemClickedListener.itemClicked(position);
        }
    }
}
