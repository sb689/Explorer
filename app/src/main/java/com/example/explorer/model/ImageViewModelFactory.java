package com.example.explorer.model;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ImageViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String mAssetId;

    public ImageViewModelFactory(String mAssetId) {
        this.mAssetId = mAssetId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ImageViewModel(mAssetId);
    }

    
}
