package com.example.explorer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import com.example.explorer.R;


public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    public static int mPosition;

    public static int getmPosition() {
        return mPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            SearchFragment fragment = new SearchFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, SearchFragment.TAG).commit();
        }

    }

    public void showSearchResponseList(){

        ListFragment listFragment = new ListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("listFragment")
                .replace(R.id.fragment_container,
                        listFragment, null).commit();
    }

    public void showDetail(int position){
        Log.d(TAG, "item clicked received in showDetail" );
        mPosition = position;
        DetailFragment detailFragment = new DetailFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("detailFragment")
                .replace(R.id.fragment_container,
                        detailFragment).commit();

    }
}
