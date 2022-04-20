package com.example.ourmp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Search extends BaseActivity implements NetworkingService.NetworkingListener, SearchAdapter.AdapterCallback, ActivityFeedRecyclerAdapter.AdapterCallback {

    String str_url = "https://represent.opennorth.ca/representatives/house-of-commons/?limit=500";
    EditText SearchText;

    ArrayList<MP> searchHistoryMPs = new ArrayList<>();
    RecyclerView recyclerViewHistoryMPs;

    ArrayList<Activity> searchHistoryBills = new ArrayList<>();
    RecyclerView recyclerViewHistoryBills;

    NetworkingService networkingService;
    JsonService jsonService;

    RecyclerView recyclerViewBills;
    ArrayList<Activity> activitiesBills = new ArrayList<>();
    ActivityFeedRecyclerAdapter recyclerAdapterBills;
    ActivityFeedRecyclerAdapter historyAdapterBills;

    ArrayList<MP> MPArrayList = new ArrayList<>();
    RecyclerView recyclerViewMPs;

    private SearchAdapter adapterMPs;
    private SearchAdapter historyAdapterMPs;

    LinearLayout RecentSearchTitle;
    Button ClearRecentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_search);

        RecentSearchTitle = findViewById(R.id.recyclerView_history_Title);
        ClearRecentSearch = findViewById(R.id.clear_recent_search);

        //initialize networking service and json service
        networkingService = ((MainApplication)getApplication()).getNetworkingService();
        jsonService = ((MainApplication)getApplication()).getJsonService();
        networkingService.listener = this;

        // Bills list
        recyclerViewBills = findViewById(R.id.recyclerView_Bills);
        recyclerAdapterBills = new ActivityFeedRecyclerAdapter(activitiesBills, this);
        recyclerViewBills.setAdapter(recyclerAdapterBills);
        recyclerViewBills.setLayoutManager(new LinearLayoutManager(this));
        networkingService.fetchBillsData();

        // Bills Search History
        recyclerViewHistoryBills = findViewById(R.id.recyclerView_history_Bills);
        LinearLayoutManager linearLayoutManagerBills = new LinearLayoutManager(this);
        linearLayoutManagerBills.setReverseLayout(true);
        linearLayoutManagerBills.setStackFromEnd(true);
        recyclerViewHistoryBills.setLayoutManager(linearLayoutManagerBills);
        historyAdapterBills = new ActivityFeedRecyclerAdapter(searchHistoryBills, Search.this);
        recyclerViewHistoryBills.setAdapter(historyAdapterBills);

        // MPs list
        MPArrayList = new ArrayList<>();
        recyclerViewMPs = findViewById(R.id.recyclerView_MPs);
        recyclerViewMPs.setLayoutManager(new LinearLayoutManager(this));
        adapterMPs = new SearchAdapter(MPArrayList, Search.this);
        recyclerViewMPs.setAdapter(adapterMPs);
        getMPsList();

        // MPs Search History
        recyclerViewHistoryMPs = findViewById(R.id.recyclerView_history_MPs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewHistoryMPs.setLayoutManager(linearLayoutManager);
        historyAdapterMPs = new SearchAdapter(searchHistoryMPs, Search.this);
        recyclerViewHistoryMPs.setAdapter(historyAdapterMPs);

        // get MP history from local Prefs
        ArrayList<MP> MPsFromStorage = PrefConfig.readListFromPref(getApplicationContext());
        if (MPsFromStorage != null) {
            RecentSearchTitle.setVisibility(View.VISIBLE);
            for (int i = 0; i < MPsFromStorage.size(); i++) {
                searchHistoryMPs.add(MPsFromStorage.get(i));
                Log.d(searchHistoryMPs.get(i).getName(), "onCreate: ");
            }
            historyAdapterMPs.update();
        }

        // get Bills history from local Prefs
        ArrayList<Activity> BillsFromStorage = PrefConfig.readListFromPrefBills(getApplicationContext());
        if (BillsFromStorage != null) {
            RecentSearchTitle.setVisibility(View.VISIBLE);
            for (int i = 0; i < BillsFromStorage.size(); i++) {
                searchHistoryBills.add(BillsFromStorage.get(i));
                Log.d(searchHistoryBills.get(i).activityTitle, "onCreate: ");
            }
            historyAdapterMPs.update();
        }

        this.SearchText = findViewById(R.id.search_input);
        this.SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                filterQuery(editable.toString());
            }
        });

        // Clear search history
        ClearRecentSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RecentSearchTitle.setVisibility(View.GONE);
                recyclerViewHistoryMPs.setVisibility(View.GONE);
                recyclerViewHistoryBills.setVisibility(View.GONE);
                PrefConfig.deleteListInPref(getApplicationContext());
                searchHistoryMPs.clear();
                searchHistoryBills.clear();
                historyAdapterMPs.update();
                historyAdapterBills.update();
            }
        });

       BottomNavigationView botNav = findViewById(R.id.botNav);

        botNav.setSelectedItemId(R.id.search);

        botNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Intent intent = getIntent();

                if (item.getItemId() == R.id.home) {
                    //Toast.makeText(getApplicationContext(), "Clicked recent events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Search.this, MainActivity.class);
                    startActivity(intent);
                }

                if (item.getItemId() == R.id.search) {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(Search.this, Search.class);
                }

                if (item.getItemId() == R.id.events) {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Search.this, Events.class);
                    startActivity(intent);
                }

                return true;
            }
        });
    }

    private void getMPsList() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, str_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayMps = jsonObject.getJSONArray("objects");

                    for (int i = 0; i < jsonArrayMps.length(); i++) {
                        MP member = new MP();
                        JSONObject jsonObject1 = jsonArrayMps.getJSONObject(i);
                        member.setName(jsonObject1.getString("name"));
                        member.setParty(jsonObject1.getString("party_name"));
                        member.setPhotoURL(jsonObject1.getString("photo_url"));
                        member.setRiding(jsonObject1.getString("district_name"));
                        MPArrayList.add(member);
                    }

                    adapterMPs.update();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    public void filterQuery(String text) {
        if (!text.isEmpty()) {
            RecentSearchTitle.setVisibility(View.GONE);
            recyclerViewHistoryMPs.setVisibility(View.GONE);
            recyclerViewHistoryBills.setVisibility(View.GONE);
            recyclerViewMPs.setVisibility(View.VISIBLE);
            recyclerViewBills.setVisibility(View.VISIBLE);
            ArrayList<MP> filteredNames = new ArrayList<>();
            for (MP mp : this.MPArrayList) {
                if (mp.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredNames.add(mp);
                    adapterMPs.update();
                }
            }
            this.adapterMPs.setFilter(filteredNames);

            ArrayList<Activity> filteredBills = new ArrayList<>();
            for (Activity bill : this.activitiesBills) {
                if (bill.activityTitle.toLowerCase().contains(text.toLowerCase())) {
                    filteredBills.add(bill);
                    recyclerAdapterBills.update();
                }
            }
            this.recyclerAdapterBills.setFilter(filteredBills);
        } else {
            if (searchHistoryMPs.size() > 0 || searchHistoryBills.size() > 0) {
                RecentSearchTitle.setVisibility(View.VISIBLE);
                recyclerViewHistoryMPs.setVisibility(View.VISIBLE);
                recyclerViewHistoryBills.setVisibility((View.VISIBLE));
            }
            recyclerViewMPs.setVisibility(View.GONE);
            recyclerViewBills.setVisibility(View.GONE);
        }
    }

    @Override
    public void APINetworkListener(String jsonString) {
    }

    @Override
    public void APINetworkingListerForImage(Bitmap image) {
    }

    @Override
    public void APIMPMoreInfoListener(String jsonString) {
    }

    @Override
    public void APIMPDescListener(String jsonString) {
    }

    @Override
    public void APIBillsListener(String jsonString) {
        ArrayList<Activity> temp;
        temp = jsonService.parseFindBills(jsonString);
        activitiesBills.addAll(temp);
        activitiesBills.sort(Comparator.comparing(obj -> obj.activityDate));
        Collections.reverse(activitiesBills);
        recyclerAdapterBills.update();
    }

    @Override
    public void APIMoreBillInfoListener(String jsonString) {

    }

    @Override
    public void APIParseBillVote(String jsonString) {

    }

    @Override

    public void APINetworkingListerForImage2(Bitmap image) {

    }

    @Override
    public void onMethodCallback(MP selectedMP) {
        for (int i = 0; i < searchHistoryMPs.size(); i++) {
            if(searchHistoryMPs.get(i).getName().equals(selectedMP.getName())) {
                searchHistoryMPs.remove(i);
            }
        }
        searchHistoryMPs.add(selectedMP);

        // add history to local Prefs
        PrefConfig.writeListInPref(getApplicationContext(), searchHistoryMPs);

        // Set search bar to empty and update history adapter
        this.SearchText.setText("");
        historyAdapterMPs.update();
    }

    @Override
    public void onMethodCallback(Activity selectedBill) {
        for (int i = 0; i < searchHistoryBills.size(); i++) {
            if(searchHistoryBills.get(i).activityTitle.equals(selectedBill.activityTitle)) {
                searchHistoryBills.remove(i);
            }
        }
        searchHistoryBills.add(selectedBill);

        // add history to local Prefs
        PrefConfig.writeListInPrefBills(getApplicationContext(), searchHistoryBills);

        // Set search bar to empty and update history adapter
        this.SearchText.setText("");
        historyAdapterBills.update();
    }
}