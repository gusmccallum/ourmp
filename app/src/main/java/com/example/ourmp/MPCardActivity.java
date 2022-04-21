package com.example.ourmp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.datastore.generated.model.Subscribed2;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MPCardActivity extends BaseActivity
        implements NetworkingService.NetworkingListener,
        DBManager.subObjCallback{

    NetworkingService networkingService;
    JsonService jsonService;
    TextView mpName, mpRiding, mpParty, mpInfo, billnum_txt, ballot_txt, recent_ballot_txt;
    ImageView img;
    Button snsBtn, emailBtn, phoneBtn, subscribeBtn, compareBtn, moreBallot_btn;
    MP mpObj;
    ArrayList<Ballot> validBollotList = new ArrayList<>(0);
    BallotsAdapter adapter;
    RecyclerView recyclerView;
    DBManager dbManager;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_mpcard);

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
                    intent = new Intent(MPCardActivity.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.search)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(MPCardActivity.this, Search.class);
                }

                if (item.getItemId() == R.id.events)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(MPCardActivity.this, Events.class);
                }

                startActivity(intent);
                return true;
            }
        });

        mpObj = getIntent().getParcelableExtra("selectedMP");

        //Perform initial query to see if user is subscribed to MP
        if (((MainApplication)getApplication()).getLogInStatus()) {
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
        compareBtn.setVisibility(View.GONE);

        requestQueue = Volley.newRequestQueue(this);

        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        networkingService.listener = this;

        networkingService.getImageData(mpObj.getPhotoURL());
        networkingService.fetchMoreMPInfo(mpObj.getName());
        fetchVotes(mpObj.getName());
        ArrayList<Ballot> shortBallotList = new ArrayList<>(0);
        if(validBollotList.size() > 3){
            for(int j=0; j<3; j++){
                shortBallotList.add(validBollotList.get(j));
            }
            adapter = new BallotsAdapter(this, shortBallotList);
        }else{
            adapter = new BallotsAdapter(this, validBollotList);
        }

        recent_ballot_txt.setText(R.string.recent_ballot_txt);
        moreBallot_btn.setVisibility(View.VISIBLE);
        compareBtn.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(adapter);
    }



    public void fetchVotes(String mpName) {
       String formattedName = (((MainApplication) getApplication()).formatName(mpName, "-"));
        String url = "https://www.ourcommons.ca/Members/en/" + formattedName + "(" + ((MainApplication)getApplication()).getMPId(mpName) + ")/votes/csv";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                String[] lines = response.split("\\r?\\n");
                int loopCount = 0;
                for (int i = 1; i < lines.length-1; i++) {
                    String[] temp = lines[i].split(",");
                    if (!temp[temp.length-1].equals("0")) {
                        loopCount++;
                        String result;
                        if (temp[5].equals("Yea")) {
                            result = "Yes";
                        }
                        else if(temp[5].equals("Nay")) {
                            result = "No";
                        }
                        else{
                            result = "";
                        }
                        String[] billDesc1 = temp[10].split("\"");
                        String[] billDesc2 = temp[11].split("\"");
                        String ballot = result;
                        String description = billDesc1[1] + "," + billDesc2[0];
                        String date = temp[8];
                        String billNum;
                        String session = temp[6]+"-"+temp[7];
                        if(temp[temp.length-1] == null){
                            billNum = "empty";
                        }else{
                           billNum  = temp[temp.length-1];
                        }

                        if(!billNum.equals("empty"))
                            validBollotList.add(new Ballot(ballot, "", "", billNum, date, session, description));

                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }
                    if (loopCount == 20) {
                        break;
                    }
                }
            } catch(Exception e) {
                Log.i("Fetchvotes exception", "10" + e.getMessage());
                Log.i("XML Stream", e.getMessage());
            }


        },
                error -> {
                    Log.i("Fetchvotes volley error", "11" + error.getMessage());
                    Log.e("VolleyError", error.getMessage());
                });
        Log.i("Fetchvotes", "12");
        requestQueue.add(request);
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
    public void APIMPDescListener(String jsonString) {
        String desc = jsonService.parseMPDesc(jsonString, mpObj.getName());
        mpInfo.setText(desc);
        //progressDialog.dismiss();
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
        if (((MainApplication)getApplication()).getLogInStatus()) {
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
            runOnUiThread(() -> {
                if (!subscribedMPs.contains(MPName)) {
                    subscribeBtn.setText(R.string.subscribe);
                }
                else {
                    subscribeBtn.setText(R.string.unfollow);
                }
            });
        }
    }

    public void WikiBtnClicked(View view) {
        Intent twitterIntent =new Intent("android.intent.action.VIEW",
                Uri.parse("https://en.wikipedia.org/wiki/"+mpObj.getName()));
        startActivity(twitterIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue != null)
            requestQueue.cancelAll(this);
    }
}

