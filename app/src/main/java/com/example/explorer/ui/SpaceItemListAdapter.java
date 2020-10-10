package com.example.explorer.ui;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explorer.R;
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
            Picasso.get().load(mItemList.get(position).getLinks().get(0).getHref()).into(mDataBinding.ivSpaceImage);
            mDataBinding.tvTitle.setText(mItemList.get(position).getData().get(0).getTitle());

            String partialDesc = mDataBinding.getRoot().getContext().getString(R.string.item_image_content_desc
                , mItemList.get(position).getData().get(0).getTitle());
            mDataBinding.ivSpaceImage.setContentDescription(partialDesc);

            mDataBinding.tvShortDesc.setText(mItemList.get(position).getData().get(0).getDescription());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "item clicked received in adapter" );
            mItemClickedListener.itemClicked(getAdapterPosition());
        }
    }
}
