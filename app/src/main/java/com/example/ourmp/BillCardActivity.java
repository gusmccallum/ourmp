package com.example.ourmp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amplifyframework.datastore.generated.model.Subscribed2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BillCardActivity extends BaseActivity implements NetworkingService.NetworkingListener, DBManager.subObjCallback{
    NetworkingService networkingService;
    JsonService jsonService;
    DBManager dbManager;

    TextView billTitle, billDesc, billDescription;

    Activity activity;
    Bill bill = null, comparedBill = null;

    Button compareBtn;
    Button subscribeBtn;
    ArrayList<PartyVote> partyVotes = new ArrayList<>();
    ArrayList<Activity> activitiesBills = new ArrayList<>(0);
    RelativeLayout comparedBillView;
    RelativeLayout noVoteView;

    TextView conservativeVote;
    TextView liberalVote;
    TextView ndpVote;
    TextView greenVote;

    ProgressBar progressBar;
    private RequestQueue mRequestQueue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_billcard);

        activity = getIntent().getParcelableExtra("bill");

        if (((MainApplication)getApplication()).getLogInStatus() == true) {
            DBManager dbManager = ((MainApplication)getApplication()).getDbManager();
            dbManager.getSubscriptionObject();
            dbManager.setSubObjCallbackInstance(this);
        }

        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        networkingService.listener = this;
        dbManager = ((MainApplication) getApplication()).getDbManager();
        dbManager.setSubObjCallbackInstance(BillCardActivity.this);

        billTitle = findViewById(R.id.bill_number);
        billDesc = findViewById(R.id.bill_desc);
        billDescription = findViewById(R.id.bill_description);
        compareBtn = findViewById(R.id.bill_compare_btn);
        subscribeBtn = findViewById(R.id.billpage_subscribe_btn);
        comparedBillView = findViewById(R.id.comparedBill);
        noVoteView = findViewById(R.id.novotes);

        conservativeVote = findViewById(R.id.conservative);
        liberalVote = findViewById(R.id.liberal);
        ndpVote = findViewById(R.id.ndp);
        greenVote = findViewById(R.id.green);
        progressBar = findViewById(R.id.progressBar);

        progressBar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);

        networkingService.fetchMoreBillInfo(activity.url);

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
                    intent = new Intent(BillCardActivity.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.search)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BillCardActivity.this, Search.class);
                }

                if (item.getItemId() == R.id.events)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BillCardActivity.this, Events.class);
                }

                startActivity(intent);
                return true;
            }
        });
        mRequestQueue = Volley.newRequestQueue(this);
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
    }

    @Override
    public void APIMoreBillInfoListener(String jsonString) {
        bill = jsonService.parseMoreBillInfo(jsonString);
        if(bill.getBillResult().equals("")){
            bill.setBillDate(activity.activityDate);
            bill.setBillDesc(activity.activityDescription);
            bill.setBillResult("unknown");
        }
        billTitle.setText("Bill Number: " + bill.getBillNum());
        if(bill.getVoteURl() != "") {
            networkingService.fetchBillVotes(bill.getVoteURl());
        }
        fetchBills(bill.getBillNum());

    }

    @Override
    public void APIParseBillVote(String jsonString) {
        partyVotes = jsonService.parseBillVote(jsonString);
        int countYes = 0;
        for(int i=0; i<partyVotes.size(); i++){
            if(partyVotes.get(i).name.equals("Conservative")){
                conservativeVote.setText(partyVotes.get(i).vote);
            }else if(partyVotes.get(i).name.equals("Green")){
                greenVote.setText(partyVotes.get(i).vote);
            }else if(partyVotes.get(i).name.equals("NDP")){
                ndpVote.setText(partyVotes.get(i).vote);
            }else if(partyVotes.get(i).name.equals("Liberal")){
                liberalVote.setText(partyVotes.get(i).vote);
            }
            if(partyVotes.get(i).vote.equals("Yes")){
                countYes+=20;
            }

        }

        progressBar.setProgress(countYes);
    }

    @Override
    public void APINetworkingListerForImage2(Bitmap image) {

    }

    public void clickCompareButton(View view){
        if(bill.getVoteURl() == ""){
            noVoteView.setVisibility(View.VISIBLE);
        }else {
            comparedBillView.setVisibility(View.VISIBLE);
        }
    }

    public void clickSubscribeButton(View view){
        if (((MainApplication)getApplication()).getLogInStatus() == true) {
            DBManager dbManager = ((MainApplication)getApplication()).getDbManager();
            //if the button = subscribe which means user has not followed the MP yet
            //String url = "https://api.openparliament.ca/bills/" + bill.getBillSession() + "/" + bill.getBillNum() + "/?format=json";
            String url = bill.getBillNum();
            if(subscribeBtn.getText().toString().equals("Subscribe")){
                //follow the MP and change the text to unfollow
                dbManager.addBillSubscription(url);
                subscribeBtn.setText(R.string.unfollow);
                Toast.makeText(this, "Subscribed!", Toast.LENGTH_SHORT).show();
            }
            //if user already followed the MP and wants to unfollow
            else{
                dbManager.removeBillSubscription(url);
                subscribeBtn.setText(R.string.subscribe);
                Toast.makeText(this, "Unfollowed!", Toast.LENGTH_SHORT).show();
            }
            dbManager.setSubObjCallbackInstance(this);
        }
        else{
            //else - not logged in
            Toast.makeText(this, "Login to save Bills Subscriptions.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getSub(Subscribed2 cbReturnSub) {
        List<String> subscribedBills = cbReturnSub.getSubscribedBills();
        if (subscribedBills != null) {
            runOnUiThread(() -> {
                for (int i = 0; i < subscribedBills.size(); i++) {
                    String billNum = subscribedBills.get(i);
                    String[] temp2 = activity.activityTitle.split(" ");
                    String currentBillNum = temp2[0];
                    if (currentBillNum.equals(billNum)) {
                        if (subscribeBtn.getText().toString().equals("Subscribe")) {
                            subscribeBtn.setText("Unsubscribe");
                        }
                    }
                    else {
                        subscribeBtn.setText("Subscribe");
                    }
                }

            });
        }
    }
    private void fetchBills(String billNum)
    {
            String url = "https://www.parl.ca/legisinfo/en/bill/44-1/" + billNum + "/json";

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        JSONArray BillsArray = response;

                        for (int i = 0; i < BillsArray.length(); i++) {
                            JSONObject BillObject = BillsArray.getJSONObject(i);
                            String billSession = BillObject.getString("ParliamentNumber") + "-" + BillObject.getString("SessionNumber");
                            String date = BillObject.getString("LatestBillEventDateTime");
                            String billResult = BillObject.getString("StatusNameEn") + " after " + BillObject.getString("LatestCompletedMajorStageNameWithChamberSuffix");
                            String billSponsorName = BillObject.getString("SponsorPersonOfficialFirstName") + " " + BillObject.getString("SponsorPersonOfficialLastName");
                            String description = BillObject.getString("LongTitleEn");
                            String text = "Session: " + billSession +"\nSponsored by: " + billSponsorName + "\nDate: " + date;
                            billDesc.setText(text);
                            billDescription.setText("Description: " + description + "\nStatus: " + billResult );
                        }

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

}
