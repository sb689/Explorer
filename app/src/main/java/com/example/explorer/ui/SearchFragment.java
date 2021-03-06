package com.example.explorer.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.explorer.AppExecutors;
import com.example.explorer.R;
import com.example.explorer.database.AppDatabase;
import com.example.explorer.database.QueryRecordEntry;
import com.example.explorer.database.YearRecordEntry;

import com.example.explorer.databinding.FragmentSearchBinding;
import com.example.explorer.model.QueryHistoryViewModel;
import com.example.explorer.model.SpaceViewModel2;
import com.example.explorer.model.YearViewModel;
import com.example.explorer.model.spaceResponse.Item;
import com.example.explorer.model.spaceResponse.SpaceResponse;
import com.example.explorer.network.NetworkUtils;
import com.example.explorer.network.SpaceApiService;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchFragment extends Fragment {

    public static final String TAG = SearchFragment.class.getSimpleName();

    private static final int RECORD_THRESHOLD = 30;

    private FragmentSearchBinding mDataBinding;
    private AppDatabase mDb;
    private List<QueryRecordEntry> mQueryRecordHistory;
    private List<YearRecordEntry> mYearRecordHistory;
    private ArrayAdapter<String> mYearAdapter;
    private ArrayAdapter<String> mQueryAdapter;


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);


        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        ViewModelProvider.Factory factory = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
        QueryHistoryViewModel queryHistoryViewModel = new ViewModelProvider(this, factory).get(QueryHistoryViewModel.class);
        queryHistoryViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<QueryRecordEntry>>() {
            @Override
            public void onChanged(List<QueryRecordEntry> recordEntries) {
                Log.d(TAG, ":::::::::  inside onChanged, recordEntries = " + recordEntries);
                mQueryRecordHistory = recordEntries;
                loadQuerySuggestions();
                if (recordEntries.size() > RECORD_THRESHOLD) {
                    deleteExtraQueries();
                }
            }
        });

        YearViewModel yearViewModel = new ViewModelProvider(this, factory).get(YearViewModel.class);
        yearViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<YearRecordEntry>>() {
            @Override
            public void onChanged(List<YearRecordEntry> yearRecordEntries) {
                mYearRecordHistory = yearRecordEntries;
                loadYearSuggestions();
                if (yearRecordEntries.size() > RECORD_THRESHOLD) {
                    deleteExtraYears();
                }
            }
        });

        mDataBinding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClicked();
            }
        });


        mDataBinding.etQueryInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean isHandled = false;
                mDataBinding.tvErrorMsg.setVisibility(View.GONE);
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    if (v == mDataBinding.etQueryInput) {
                        mDataBinding.etQueryInput.clearFocus();
                        mDataBinding.etStartYearInput.requestFocus();
                        mDataBinding.etStartYearInput.setCursorVisible(true);
                        isHandled = true;
                    } else if (v == mDataBinding.etStartYearInput) {
                        mDataBinding.etStartYearInput.clearFocus();
                        mDataBinding.etEndYearInput.requestFocus();
                        mDataBinding.etEndYearInput.setCursorVisible(true);
                        isHandled = true;
                    } else if (v == mDataBinding.etEndYearInput) {
                        mDataBinding.etEndYearInput.clearFocus();
                        mDataBinding.etEndYearInput.setCursorVisible(false);
                        mDataBinding.buttonSearch.requestFocus();
                        isHandled = true;
                        searchClicked();
                    }
                }

                return isHandled;

            }
        });

        return mDataBinding.getRoot();
    }


    private void deleteExtraQueries() {

        int noOfRecordToBeDeleted = mQueryRecordHistory.size() - RECORD_THRESHOLD;
        List<QueryRecordEntry> toBeDeleted = new ArrayList<>();
        for (int i = 0; i < noOfRecordToBeDeleted; i++) {
            toBeDeleted.add(mQueryRecordHistory.get(i));
        }
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.searchRecordDao().deleteQueries(toBeDeleted);
            }
        });
    }

    private void deleteExtraYears() {

        int noOfRecordToBeDeleted = mYearRecordHistory.size() - RECORD_THRESHOLD;
        List<YearRecordEntry> toBeDeleted = new ArrayList<>();
        for (int i = 0; i < noOfRecordToBeDeleted; i++) {
            toBeDeleted.add(mYearRecordHistory.get(i));
        }
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.yearRecordDao().deleteYears(toBeDeleted);
            }
        });
    }


    private void loadYearSuggestions() {

        List<String> yearSuggestions = new ArrayList<String>();
        for (int i = 0; i < mYearRecordHistory.size(); i++) {
            String year = mYearRecordHistory.get(i).getYear();
            yearSuggestions.add(year);
        }
        mYearAdapter = new ArrayAdapter<String>(requireActivity(),
                android.R.layout.simple_list_item_1, yearSuggestions);
        mDataBinding.etStartYearInput.setAdapter(mYearAdapter);
        mDataBinding.etEndYearInput.setAdapter(mYearAdapter);
    }


    private void loadQuerySuggestions() {

        List<String> mQuerySuggestions = new ArrayList<String>();
        for (int i = 0; i < mQueryRecordHistory.size(); i++) {
            String query = mQueryRecordHistory.get(i).getSearchKey();
            mQuerySuggestions.add(query);
        }
        mQueryAdapter = new ArrayAdapter<String>(requireActivity(),
                android.R.layout.simple_list_item_1, mQuerySuggestions);
        mDataBinding.etQueryInput.setAdapter(mQueryAdapter);

    }


    private void showErrorMessage(int errorTag) {

        switch (errorTag) {
            case 1:
                mDataBinding.tvErrorMsg.setText(getString(R.string.main_error_msg_no_network));
                break;
            case 3:
                mDataBinding.tvErrorMsg.setText(getString(R.string.main_error_msg_no_data_found));
                break;
            default:
                mDataBinding.tvErrorMsg.setText(getString(R.string.main_error_msg_general));
        }

        mDataBinding.pbMain.setVisibility(View.INVISIBLE);
        mDataBinding.tvErrorMsg.setVisibility(View.VISIBLE);
        mDataBinding.buttonSearch.setEnabled(true);
    }

    private void showProgressBar() {
        mDataBinding.buttonSearch.setEnabled(false);
        mDataBinding.pbMain.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mDataBinding.pbMain.setVisibility(View.INVISIBLE);
        mDataBinding.buttonSearch.setEnabled(true);

    }


    public void searchClicked() {

        if (!NetworkUtils.isNetworkConnected(requireActivity())) {
            showErrorMessage(NetworkUtils.ERROR_TAG_NO_NETWORK);
            return;
        }

        //check if shotKeyboard is still open then close it
        InputMethodManager imm = (InputMethodManager)
                requireActivity().getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(mDataBinding.getRoot().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Exception ex) {
            Log.d(TAG, "closing keyboard attempt ended in NullPointerException");
        }

        mDataBinding.tvErrorMsg.setVisibility(View.INVISIBLE);
        String query = mDataBinding.etQueryInput.getText().length() > 0 ?
                mDataBinding.etQueryInput.getText().toString() : null;

        String yearStart = mDataBinding.etStartYearInput.getText().length() > 0 ?
                mDataBinding.etStartYearInput.getText().toString() : null;

        String yearEnd = mDataBinding.etEndYearInput.getText().length() > 0 ?
                mDataBinding.etEndYearInput.getText().toString() : null;

        if (query == null && yearStart == null && yearEnd == null) {

            Toast.makeText(getActivity(), getString(R.string.toast_search_criteria_required_msg), Toast.LENGTH_LONG).show();

        } else {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SpaceApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            SpaceApiService service = retrofit.create(SpaceApiService.class);
            showProgressBar();
            Call<SpaceResponse> call = service.getResult(query, yearStart, yearEnd,
                    SpaceApiService.PARAM_MEDIA_TYPE);

            call.enqueue(new Callback<SpaceResponse>() {
                @Override
                public void onResponse(Call<SpaceResponse> call, Response<SpaceResponse> response) {
                    Log.d(TAG, ":::::::::::::: call succeed, " + response.toString());
                    Log.d(TAG, ":::::::::::::: call succeed, " + response.body());


                    if (response.code() == 200) {
                        SpaceResponse spaceResponse = response.body();
                        List<Item> items = spaceResponse.getCollection().getItems();
                        Log.d(TAG, ":::::::::::::: inside onResponse, items.size = " + items.size());
                        if (items != null && items.size() > 0) {

                            SpaceViewModel2 viewModel = new ViewModelProvider(requireActivity()).get(SpaceViewModel2.class);
                            viewModel.setItemList(items);
                            saveQuery(query, yearStart, yearEnd);

                            hideProgressBar();

                            ((SearchActivity)requireActivity()).resultFound();

                        } else {
                            showErrorMessage(NetworkUtils.ERROR_TAG_NO_DATA_FOUND);
                        }
                    } else {

                        Log.d(TAG, ":::::::::::::: call succeed with an error, " + response.toString());
                    }

                }

                @Override
                public void onFailure(Call<SpaceResponse> call, Throwable t) {
                    showErrorMessage(NetworkUtils.ERROR_TAG_GENERAL);
                    Log.d(TAG, ":::::::::::::: retrofit call failed, ");
                    Log.d(TAG, ":::::::::::::: retrofit call failed, " + t.getMessage());

                }
            });

        }
    }


    private void saveQuery(String query, String yearStart, String yearEnd) {

        if (query != null && !query.isEmpty()) {
            QueryRecordEntry entry = new QueryRecordEntry(query, System.currentTimeMillis());
            //log events analytics
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.analytics_search_key));
            bundle.putString(FirebaseAnalytics.Param.VALUE, query);
            SearchActivity.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.searchRecordDao().insertQuery(entry);
                }
            });
        }
        if (yearStart != null && !yearStart.isEmpty()) {
            YearRecordEntry entry = new YearRecordEntry(yearStart, System.currentTimeMillis());
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.yearRecordDao().insertYear(entry);
                }
            });
        }
        if (yearEnd != null && !yearEnd.isEmpty()) {

            YearRecordEntry entry = new YearRecordEntry(yearEnd, System.currentTimeMillis());
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.yearRecordDao().insertYear(entry);
                }
            });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        super.onDestroy();
        mDataBinding.buttonSearch.setOnClickListener(null);
        mDb = null;
        mQueryAdapter = null;
        mYearAdapter = null;
        mDataBinding.etStartYearInput.setAdapter(null);
        mDataBinding.etEndYearInput.setAdapter(null);
        mDataBinding.etQueryInput.setOnEditorActionListener(null);
        mDataBinding.etQueryInput.setAdapter(null);
        mDataBinding = null;
        mQueryRecordHistory = null;
        mYearRecordHistory = null;

    }


}
