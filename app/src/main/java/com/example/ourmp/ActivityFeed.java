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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import java.util.List;

public class ActivityFeed extends BaseActivity implements View.OnClickListener, DBManager.subObjCallback {



    //Views
    RecyclerView activityList;
    RecyclerView activityList2;
    AFRecyclerAdapter recyclerAdapter;
    AFRecyclerAdapter recyclerAdapter2;

    //Variables holding data
    ArrayList<Activity> activities = new ArrayList<>();
    ArrayList<Activity> MpActivities = new ArrayList<>();
    ArrayList<MP> allMPs = new ArrayList<>();
    int currentMP;

    ProgressDialog progressDialog;
    TextView emptyMessage;
    private RequestQueue billRequestQueue;
    private RequestQueue voteRequestQueue;
    Button changeStateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_feed);
        if (((MainApplication) getApplication()).getLogInStatus()) {
            //check if user log in or not
            DBManager dbManager = ((MainApplication) getApplication()).getDbManager();
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

            billRequestQueue = Volley.newRequestQueue(this);
            voteRequestQueue = Volley.newRequestQueue(this);
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
    public void getSub(Subscribed2 cbReturnSub) {
        List<String> subscribedMPs= cbReturnSub.getSubscribedMPs();
        List<String> subscribedBills = cbReturnSub.getSubscribedBills();

        if (subscribedMPs != null) {

            for (int j = 0; j < subscribedMPs.size(); j++) {
                currentMP=j;
                String formattedName = (((MainApplication) getApplication()).formatName(subscribedMPs.get(j), "-"));
                String imageURL = "https://api.openparliament.ca/media/polpics/" + formattedName.toLowerCase() +".jpg";
                MP newMp = new MP();
                newMp.setName(subscribedMPs.get(j));
                newMp.setPhotoURL(imageURL);
                allMPs.add(newMp);
                new DownloadImage(allMPs.get(currentMP)).execute(allMPs.get(currentMP).getPhotoURL());
                fetchVotes(subscribedMPs.get(j));
            }
        }

        if (subscribedBills != null) {
            for (int i = 0; i < subscribedBills.size(); i++) {
                fetchBills(subscribedBills.get(i));
            }
        }


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
                        JSONObject BillObject = response.getJSONObject(0);
                        String billNum = BillObject.getString("NumberCode");
                        String billSession = BillObject.getString("ParliamentNumber") + "-" + BillObject.getString("SessionNumber");
                        String date = BillObject.getString("LatestBillEventDateTime");
                        String billResult = BillObject.getString("StatusNameEn") + " after " + BillObject.getString("LatestCompletedMajorStageNameWithChamberSuffix");
                        String billSponsorName = BillObject.getString("SponsorPersonOfficialFirstName") + " " + BillObject.getString("SponsorPersonOfficialLastName");
                        String description = BillObject.getString("LongTitleEn");
                        activities.add(new Activity(null, "Bill " + billNum + " in session " + billSession, "Bill is " + billResult + "." + description + ". Sponsored by " + billSponsorName + ".", "Updated: " + date, ""));
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Log.i("Fetchbills Dataset changed", billNum + " " + billSession + " " + date + " " + billResult + " " + description);
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        });


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

        billRequestQueue.add(request);

    }

    private void fetchVotes(String mpName) {
                String formattedName = (((MainApplication) getApplication()).formatName(mpName, "-"));
                String url = "https://www.ourcommons.ca/Members/en/" + formattedName + "(" + ((MainApplication)getApplication()).getMPId(mpName) + ")/votes/csv";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    String lines[] = response.split("\\r?\\n");
                                    int loopCount = 0;
                                    for (int i = 1; i < lines.length-1; i++) {
                                        String[] temp = lines[i].split(",");
                                        if (!temp[temp.length-1].equals("0")) {
                                            loopCount++;
                                            String name = temp[0] + " " + temp[1];
                                            String result;
                                            if (temp[5].equals("Yea")) {
                                                result = "yes";
                                            }
                                            else {
                                                result = "no";
                                            }
                                            String billDesc1[] = temp[10].split("\"");
                                            String billDesc2[] = temp[11].split("\"");
                                            String description = "Voted " + result + " on the " + billDesc1[1] + "," + billDesc2[0];
                                            String date = temp[8];
                                            MpActivities.add(new Activity(allMPs.get(currentMP).getPhoto(), name, description, date, ""));
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    recyclerAdapter2.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                        if (loopCount == 5) {
                                            break;
                                        }
                                    }
                                } catch(Exception e) {
                                    Log.e("XML Stream", e.getMessage());
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            Log.e("VolleyError", error.getMessage());
                            }

                        });
                voteRequestQueue.add(request);



    }




}