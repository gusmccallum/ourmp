package com.example.ourmp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Subscribed2;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityFeed2 extends BaseActivity implements DBManager.subObjCallback {



    //Views
    RecyclerView activityList;
    ActivityFeedRecyclerAdapter recyclerAdapter;
    private RequestQueue mpRequestQueue;
    private RequestQueue billRequestQueue;

    //Variables holding data
    ArrayList<Activity> activities = new ArrayList<>();
    ArrayList<MP> allMPs;
    ArrayList<String> mpNames;
    List<String> subscribedMPs;
    MP mpObj;
    int currentMP = 0;
    ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);
    ArrayList<Ballot> tempbollotArray = new ArrayList<>(0);
    ArrayList<Ballot> validBollotList = new ArrayList<>(0);
    TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_feed);

        //check if user log in or not
        if (((MainApplication)getApplication()).getLogInStatus() == true) {

            DBManager dbManager = ((MainApplication)getApplication()).getDbManager();
            dbManager.getSubscriptionObject();
            dbManager.setSubObjCallbackInstance(this);


            //initialize views
            activityList = findViewById(R.id.recyclerView_Bills);
            recyclerAdapter = new ActivityFeedRecyclerAdapter(activities, this);
            activityList.setAdapter(recyclerAdapter);
            activityList.setLayoutManager(new LinearLayoutManager(this));
            mpRequestQueue = Volley.newRequestQueue(this);
            billRequestQueue = Volley.newRequestQueue(this);

        }else{
            Toast.makeText(this, "Sign in to view Activity Feed", Toast.LENGTH_SHORT).show();
        }

        BottomNavigationView botNav = findViewById(R.id.botNav);

        botNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Intent intent = getIntent();

                if (item.getItemId() == R.id.home)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked recent events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(ActivityFeed2.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.search)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(ActivityFeed2.this, Search.class);
                }

                if (item.getItemId() == R.id.events)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(ActivityFeed2.this, Events.class);
                }

                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void getSub(Subscribed2 cbReturnSub) {

        if (cbReturnSub.getSubscribedBills() != null) {

        }
    }

    private void fetchMP()
    {
        String url = "https://api.openparliament.ca/votes/?session=44-1&format=json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONArray BillsArray = response.getJSONArray("objects");

                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });

        mpRequestQueue.add(request);
    }

    private void fetchBills(String billNum)
    {

        String url = "https://www.parl.ca/legisinfo/en/bill/44-1/" + billNum + "/json";


        JsonObjectRequest billInfoRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONObject BillObject = response.getJSONObject("0");
                    String title = billNum;
                    String description = BillObject.getString("LongTitleEn");
                    String status = BillObject.getString("StatusNameEn") + " after " + BillObject.getString("LatestCompletedMajorStageNameWithChamberSuffix");
                    String date = BillObject.getString("LatestBillEventDateTime");
                    String billSponsorName = BillObject.getString("SponsorPersonOfficialFirstName") + "%20" + BillObject.getString("SponsorPersonOfficialLastName");
                    Activity newBillActivity = new Activity(null, title, description, status, date, billSponsorName);
                    activities.add(newBillActivity);
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });

        billRequestQueue.add(billInfoRequest);


    }
}
