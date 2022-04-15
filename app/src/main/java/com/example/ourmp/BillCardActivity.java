package com.example.ourmp;

import android.content.Intent;
import android.graphics.Bitmap;
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

import java.util.ArrayList;

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
    public void APIBallotListener(String jsonString) {

    }

    @Override
    public void APIVoteListener(String jsonString) {

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
        String description = "Session: " + bill.getBillSession() + "\nStatus: " + bill.getBillResult() + "\nDate: " + bill.getBillDate();
        billDesc.setText(description);
        billDescription.setText("Description: " + bill.getBillDesc());
        if(bill.getVoteURl() != "") {
            networkingService.fetchBillVotes(bill.getVoteURl());
        }

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
            String url = "https://api.openparliament.ca/bills/" + bill.getBillSession() + "/" + bill.getBillNum() + "/?format=json";
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

    }
}
