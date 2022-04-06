package com.example.ourmp;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.datastore.generated.model.Subscribed2;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MPCardActivity extends BaseActivity
        implements NetworkingService.NetworkingListener, DBManager.subObjCallback{

    NetworkingService networkingService;
    JsonService jsonService;
    TextView mpName, mpRiding, mpParty, mpInfo, billnum_txt, ballot_txt, recent_ballot_txt;
    ImageView img;
    Button snsBtn, emailBtn, phoneBtn, subscribeBtn, compareBtn, moreBallot_btn;
    MP mpObj;
    ArrayList<Ballot> allBallotFromMP;
    ArrayList<Ballot> tempbollotArray = new ArrayList<>(0);
    ArrayList<Ballot> validBollotList = new ArrayList<>(0);
    BallotsAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    DBManager dbManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_mpcard);


        mpObj = getIntent().getParcelableExtra("selectedMP");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //Perform initial query to see if user is subscribed to MP
        if (((MainApplication)getApplication()).getLogInStatus() == true) {
            DBManager dbManager = ((MainApplication)getApplication()).getDbManager();
            dbManager.getSubscriptionObject();
            dbManager.setSubObjCallbackInstance(this);
        }


        mpName = findViewById(R.id.mppage_name_txt);
        mpRiding = findViewById(R.id.mppage_riding_txt);
        mpParty = findViewById(R.id.mppage_party_txt);
        mpInfo = findViewById(R.id.mppage_info_txt);
        img = findViewById(R.id.mppage_img);
        snsBtn = findViewById(R.id.sns_btn);
        emailBtn = findViewById(R.id.email_btn);
        phoneBtn = findViewById(R.id.phone_btn);
        subscribeBtn = findViewById(R.id.mppage_subscribe_btn);
        compareBtn = findViewById(R.id.compare_btn);
        moreBallot_btn = findViewById(R.id.moreBallot_btn);
        billnum_txt = findViewById(R.id.billnum_txt);
        ballot_txt = findViewById(R.id.ballot_txt);
        recent_ballot_txt = findViewById(R.id.recent_ballot_txt);
        recyclerView = findViewById(R.id.ballot_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mpName.setText(mpObj.getName());
        mpRiding.setText(mpObj.getRiding());
        mpParty.setText(mpObj.getParty());
        recent_ballot_txt.setText(R.string.wait_voteList);
        moreBallot_btn.setVisibility(View.GONE);

        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        dbManager = ((MainApplication) getApplication()).getDbManager();
        dbManager.setSubObjCallbackInstance(MPCardActivity.this);
        networkingService.listener = this;

        networkingService.getImageData(mpObj.getPhotoURL());
        networkingService.fetchMoreMPInfo(mpObj.getName());

    }

    @Override
    public void APINetworkListener(String jsonString) {
    }

    @Override
    public void APINetworkingListerForImage(Bitmap image) {
        img.setImageBitmap(image);
    }

    @Override
    public void APIMPMoreInfoListener(String jsonString) {
        MP tempMp = jsonService.parseMoreInfoAPI(jsonString);
        mpObj.setEmail(tempMp.getEmail());
        mpObj.setPhone(tempMp.getPhone());
        mpObj.setBallotURL(tempMp.getBallotURL());
        mpObj.setTwitter(tempMp.getTwitter());
        //there might not be twitter info, in case it's empty string

        networkingService.fetchMPDesc(mpObj.getName());
    }

    @Override
    public void APIBallotListener(String jsonString) {

    }
    @Override
    public void APIVoteListener(String jsonString) {
        tempbollotArray.add(jsonService.parseVote(jsonString));
        //list date and bill desc, same size with allBollotFromMP
        if(tempbollotArray.size() == 40){
            //copy all date and bill number
            for(int i=0; i<allBallotFromMP.size(); i++){

                if(!tempbollotArray.get(i).getBillNum().equals("null")){
                    allBallotFromMP.get(i).setBillNum(tempbollotArray.get(i).getBillNum());
                    allBallotFromMP.get(i).setDate(tempbollotArray.get(i).getDate());
                    allBallotFromMP.get(i).setSession(tempbollotArray.get(i).getSession());
                    allBallotFromMP.get(i).setDesc(tempbollotArray.get(i).getDesc());
                }

            }
            //choose ballots only that has valid bill number
            for(int j=0; j<allBallotFromMP.size(); j++){
                if(allBallotFromMP.get(j).getBillNum() != null){
                    validBollotList.add(allBallotFromMP.get(j));
                }
            }

            ArrayList<Ballot> shortBallotList = new ArrayList<>(0);
            if(validBollotList.size() > 5){
                for(int j=0; j<5; j++){
                    shortBallotList.add(validBollotList.get(j));
                }
                adapter = new BallotsAdapter(this, shortBallotList);
            }else{
                adapter = new BallotsAdapter(this, validBollotList);
            }
            recent_ballot_txt.setText(R.string.recent_ballot_txt);
            moreBallot_btn.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void APIMPDescListener(String jsonString) {
        String desc = jsonService.parseMPDesc(jsonString, mpObj.getName());
        mpInfo.setText(desc);
        //networkingService.fetchBallot(mpObj.getBallotURL());
        VolleyFetchBallotAPI();
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

    public void SNSBtnClicked(View view){
        if (mpObj.getTwitter().isEmpty()) {
            Toast.makeText(this, "No Twitter found for MP.", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent twitterIntent =new Intent("android.intent.action.VIEW",
                    Uri.parse("https://twitter.com/"+mpObj.getTwitter()));
            startActivity(twitterIntent);
        }
    }

    public void PhoneBtnClicked(View view){
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
        phoneIntent.setData(Uri.parse("tel:"+mpObj.getPhone()));
        startActivity(phoneIntent);
    }

    public void EmailBtnClicked(View view){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"+mpObj.getEmail()));
        startActivity(emailIntent);
    }


    public void SeemoreBtnClicked(View view) {
        Intent intent = new Intent(this, BallotListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ballotList", validBollotList);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    public void MPCompareBtnClicked(View view) {
        Intent intent = new Intent(this, CompareMPActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ballotList", validBollotList);
        intent.putExtra("bundle",bundle);
        intent.putExtra("MPObj", mpObj);
        startActivity(intent);
    }

    public void SubscribeBtnClickedInMPCard(View view) {
        //if(check if logged - yes)
        if (((MainApplication)getApplication()).getLogInStatus() == true) {
            DBManager dbManager = ((MainApplication)getApplication()).getDbManager();
            //if the button = subscribe which means user has not followed the MP yet
            if(subscribeBtn.getText().toString().equals("Subscribe")){
                //follow the MP and change the text to unfollow
                dbManager.addMPSubscription(mpName.getText().toString());
                subscribeBtn.setText(R.string.unfollow);
                //subscribeBtn.setEnabled(false);
                Toast.makeText(this, "Subscribed!", Toast.LENGTH_SHORT).show();
            }
            //if user already followed the MP and wants to unfollow
            else{
                //unfollow and change the text to subscribe
                dbManager.removeMPSubscription(mpName.getText().toString());
                subscribeBtn.setText(R.string.subscribe);
                //subscribeBtn.setEnabled(false);
                Toast.makeText(this, "Unsubscribed!", Toast.LENGTH_SHORT).show();
            }


            dbManager.setSubObjCallbackInstance(this);
        }
        else{
            //else - not logged in
            Toast.makeText(this, "Login to save MP Subscriptions.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getSub(Subscribed2 cbReturnSub) {
        String MPName = mpName.getText().toString();
        List<String> subscribedMPs = cbReturnSub.getSubscribedMPs();
        if (subscribedMPs != null) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (subscribedMPs.indexOf(MPName) == -1) {
                        subscribeBtn.setText(R.string.subscribe);
                    }
                    else {
                        subscribeBtn.setText(R.string.unfollow);
                    }
                }
            });
        }
    }
    public void VolleyFetchBallotAPI(){

        final String url = "https://api.openparliament.ca/" + mpObj.getBallotURL() + "&limit=40";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    allBallotFromMP = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);// root
                    JSONArray BallotArray = jsonObject.getJSONArray("objects");

                    for (int i = 0 ; i< BallotArray.length(); i++){
                        Ballot newBallot = new Ballot();
                        JSONObject BallotObject = BallotArray.getJSONObject(i);

                        newBallot.setBallot(BallotObject.getString("ballot"));
                        newBallot.setPoliticianURL(BallotObject.getString("politician_url"));
                        newBallot.setVoteURL(BallotObject.getString("vote_url"));

                        if(!newBallot.getBallot().equals("Yes") && !newBallot.getBallot().equals("No")){
                            newBallot.setBallot("");
                        }

                        allBallotFromMP.add(newBallot);
                        //VolleyFetchVoteAPI(i, "https://api.openparliament.ca"+newBallot.getVoteURL());
                        progressDialog.dismiss();
                        networkingService.fetchVote(newBallot.getVoteURL());

                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void WikiBtnClicked(View view) {
        Intent twitterIntent =new Intent("android.intent.action.VIEW",
                Uri.parse("https://en.wikipedia.org/wiki/"+mpObj.getName()));
        startActivity(twitterIntent);
    }

}

