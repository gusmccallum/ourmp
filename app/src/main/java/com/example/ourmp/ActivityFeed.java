package com.example.ourmp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Subscribed2;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.List;

public class ActivityFeed extends BaseActivity implements View.OnClickListener, NetworkingService.NetworkingListener, DBManager.subObjCallback {

    //Services
    NetworkingService networkingService;
    JsonService jsonService;
    DBManager dbManager;

    List<String> subscribedMPs;
    List<String> subscribedBills;

    //Views
    RecyclerView activityList;
    RecyclerView activityList2;
    ActivityFeedBaseAdapter adapter;
    AFRecyclerAdapter recyclerAdapter;
    AFRecyclerAdapter recyclerAdapter2;

    //Variables holding data
    ArrayList<Activity> activities = new ArrayList<>();
    ArrayList<Activity> MpActivities = new ArrayList<>();
    ArrayList<MP> allMPs;
    ArrayList<String> mpNames;
    MP mpObj;
    int currentMP = 0;
    ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);
    ArrayList<Ballot> tempbollotArray = new ArrayList<>(0);
    ArrayList<Ballot> validBollotList = new ArrayList<>(0);
    ProgressDialog progressDialog;
    TextView emptyMessage;
    private RequestQueue mRequestQueue;
    Button changeStateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_feed);
        if (((MainApplication) getApplication()).getLogInStatus() == true) {
            //check if user log in or not
            dbManager = ((MainApplication) getApplication()).getDbManager();
            dbManager.getSubscriptionObject();
            dbManager.setSubObjCallbackInstance(this);


            //initialize views
            activityList = findViewById(R.id.recyclerView_Bills);
            recyclerAdapter = new AFRecyclerAdapter(activities, this);
            activityList.setAdapter(recyclerAdapter);
            activityList.setLayoutManager(new LinearLayoutManager(this));

            activityList2 = findViewById(R.id.recyclerView_MPs);
            recyclerAdapter2 = new AFRecyclerAdapter(MpActivities, this);
            activityList2.setAdapter(recyclerAdapter2);
            activityList2.setLayoutManager(new LinearLayoutManager(this));

            changeStateBtn = findViewById(R.id.changeBtn);
            changeStateBtn.setOnClickListener(this);

            //initialize networking service and json service
            networkingService = ((MainApplication) getApplication()).getNetworkingService();
            jsonService = ((MainApplication) getApplication()).getJsonService();
            networkingService.listener = this;

            mRequestQueue = Volley.newRequestQueue(this);
        } else {
            Toast.makeText(this, "Sign in to view Activity Feed", Toast.LENGTH_SHORT).show();
        }

        BottomNavigationView botNav = findViewById(R.id.botNav);

        botNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = getIntent();

                if (item.getItemId() == R.id.home) {
                    //Toast.makeText(getApplicationContext(), "Clicked recent events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(ActivityFeed.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.search) {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(ActivityFeed.this, Search.class);
                }

                if (item.getItemId() == R.id.events) {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(ActivityFeed.this, Events.class);
                }

                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.changeBtn:
                if(changeStateBtn.getText().equals("View Bills")){
                    activityList.setVisibility(View.VISIBLE);
                    activityList2.setVisibility(View.INVISIBLE);
                    changeStateBtn.setText("View MP's activities");
                }else{
                    activityList.setVisibility(View.INVISIBLE);
                    activityList2.setVisibility(View.VISIBLE);
                    changeStateBtn.setText("View Bills");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        allMPs = ((MainApplication) getApplication()).allMPs;
    }

    @Override
    public void APINetworkListener(String jsonString) {
    }

    @Override
    public void APIBillsListener(String jsonString) {
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
    public void APINetworkingListerForImage(Bitmap image) {
    }

    @Override
    public void APIMPMoreInfoListener(String jsonString) {
        MP tempMp = jsonService.parseMoreInfoAPI(jsonString);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            tempMp.setName(jsonObject.getString("name"));
            tempMp.setPhotoURL("https://api.openparliament.ca" + jsonObject.getString("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        allMPs.add(tempMp);
        allMPs.get(currentMP).setBallotURL(tempMp.getBallotURL());
        new DownloadImage(allMPs.get(currentMP)).execute(allMPs.get(currentMP).getPhotoURL());
        networkingService.fetchBallot(allMPs.get(currentMP).getBallotURL());
        //VolleyFetchBallotAPI();
    }

    @Override
    public void APIBallotListener(String jsonString) {
        allBallotFromMP = jsonService.parseBallots(jsonString);
        for (int i = 0; i < allBallotFromMP.size(); i++) {
            networkingService.fetchVote(allBallotFromMP.get(i).getVoteURL());
        }
    }

    @Override
    public void APIVoteListener(String jsonString) {
        tempbollotArray.add(jsonService.parseVote(jsonString));
        //list date and bill desc, same size with allBollotFromMP
        if (tempbollotArray.size() == allBallotFromMP.size()) {
            for (int i = 0; i < allBallotFromMP.size(); i++) {
                allBallotFromMP.get(i).setBillNum(tempbollotArray.get(i).getBillNum());
                allBallotFromMP.get(i).setDate(tempbollotArray.get(i).getDate());
            }

            for (int i = 0; i < allBallotFromMP.size(); i++) {
                if (!allBallotFromMP.get(i).getBillNum().equals("null")) {
                    MpActivities.add(new Activity(allMPs.get(currentMP).getPhoto(), "MP " + allMPs.get(currentMP).getName(), "Voted " + allBallotFromMP.get(i).getBallot() + " on " + allBallotFromMP.get(i).getBillNum(), allBallotFromMP.get(i).getDate(), ""));
                }
            }
            tempbollotArray.clear();
            allBallotFromMP.clear();
            recyclerAdapter2.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    @Override
    public void APIMPDescListener(String jsonString) {
    }

    @Override
    public void getSub(Subscribed2 cbReturnSub) {
        subscribedMPs= cbReturnSub.getSubscribedMPs();
        subscribedBills = cbReturnSub.getSubscribedBills();
        if (subscribedBills != null) {
            for (int i = 0; i < subscribedBills.size(); i++) {
                fetchBills(subscribedBills.get(i));
            }
        }
        if (subscribedMPs != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            for (int j = 0; j < subscribedBills.size(); j++) {
                currentMP=j;
                networkingService.fetchMoreMPInfo(subscribedMPs.get(j));
            }
        }
//progressDialog.dismiss();
        /*List<String> subscribedMPs = cbReturnSub.getSubscribedMPs();
        if (subscribedMPs != null) {
            for (int i = 0; i < subscribedMPs.size(); i++) {
                currentMP = i;
                networkingService.fetchMoreMPInfo(subscribedMPs.get(i));
            }

                    fetchBills("C-5");
        currentMP = 0;
        networkingService.fetchMoreMPInfo("Han Dong");
        }*/
    }

    public static class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        MP member;

        public DownloadImage(MP member) {
            this.member = member;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            member.setPhoto(result);
        }
    }

    private void fetchBills(String billNum) {
        String url = "https://www.parl.ca/legisinfo/en/bill/44-1/" + billNum + "/json";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray BillsArray = response;

                    for (int i = 0; i < BillsArray.length(); i++) {
                        JSONObject BillObject = BillsArray.getJSONObject(i);
                        String billNum = BillObject.getString("NumberCode");
                        String billSession = BillObject.getString("ParliamentNumber") + "-" + BillObject.getString("SessionNumber");
                        String date = BillObject.getString("LatestBillEventDateTime");
                        String billResult = BillObject.getString("StatusNameEn") + " after " + BillObject.getString("LatestCompletedMajorStageNameWithChamberSuffix");
                        String billSponsorName = BillObject.getString("SponsorPersonOfficialFirstName") + " " + BillObject.getString("SponsorPersonOfficialLastName");
                        String description = BillObject.getString("LongTitleEn");
                        activities.add(new Activity(null, "Bill " + billNum + " in session " + billSession, "Bill is " + billResult + "." + description + ". Sponsored by " + billSponsorName + ".", "Updated: " + date, ""));

                    }
                    recyclerAdapter.notifyDataSetChanged();
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

        mRequestQueue.add(request);

    }
/*
    private void fetchMps(String mpName) {
        String url;
        String formattedName = formatName(mpName, "-");
        url = "https://api.openparliament.ca/politicians/" + formattedName.toLowerCase() + "/?format=json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response;
                    MP tempMp = new MP();
                    String name = jsonObject.getString("name");
                    String photoURL = jsonObject.getString("image");
                    JSONObject relatedObject = jsonObject.getJSONObject("related");
                    String ballotURL = relatedObject.getString("ballots_url");

                    //tempMp.setName(name);
                    //tempMp.setPhotoURL("https://api.openparliament.ca" + response.getString("image"));
                    //tempMp.setBallotURL(ballotURL);
                    //allMPs.add(tempMp);
                    //new DownloadImage(allMPs.get(currentMP)).execute(allMPs.get(currentMP).getPhotoURL());
                    //networkingService.fetchBallot(allMPs.get(currentMP).getBallotURL());
                    MpActivities.add(new Activity(null, name, photoURL, ballotURL, ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                recyclerAdapter2.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    public String formatName(String fullName, String replacement) {

        String formattedStr;

        if (fullName.equals("Robert J. Morrissey")) {
            formattedStr = "Bobby Morrissey";
        } else if (fullName.equals("Candice Bergen")) {
            formattedStr = "Candice Hoeppner";
        } else {

            formattedStr = fullName;
            if (fullName.equals("Harjit S. Sajjan")) {
                formattedStr = "Harjit S Sajjan";
            }
            //change all non-enlgish letter to english
            formattedStr = formattedStr.replace("\u00e9", "e")
                    .replace("\u00e8", "e")
                    .replace("\u00e7", "c")
                    .replace("\u00c9", "e")
                    .replace("\u00eb", "e");
            //remove all '
            formattedStr = formattedStr.replace("'", "");
            //remove middle name with dot(.)
            int dot = formattedStr.indexOf(".");
            if (dot > -1) {
                //ex - Michael V. McLeod, dot=9
                String s2 = formattedStr.substring(dot + 1); //" McLeod"
                String s1 = formattedStr.substring(0, dot - 2); // "Michael"
                formattedStr = s1 + s2; //"Michael McLeod"
            }
        }

        //replace white space with replacement - or %20
        if (formattedStr.contains(" ")) {
            String[] splitStr = formattedStr.trim().split("\\s+");
            //ex) Adam, van, Koeverden
            String str = "";
            for (int i = 0; i < splitStr.length; i++) { //3
                if (i == splitStr.length - 1) {
                    str += splitStr[i]; //str = Adam-van-Koeverdeny
                } else {
                    str += splitStr[i] + replacement; //str = Adam-van-
                }
            }
            formattedStr = str;
        }

        return formattedStr;
    }*/
}