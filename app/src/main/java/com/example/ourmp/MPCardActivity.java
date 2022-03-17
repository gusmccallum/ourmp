package com.example.ourmp;

import androidx.appcompat.app.AppCompatActivity;
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

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Subscribed;

import java.util.ArrayList;
import java.util.List;

public class MPCardActivity extends BaseActivity
        implements NetworkingService.NetworkingListener, DBManager.subObjCallback{

    NetworkingService networkingService;
    JsonService jsonService;
    TextView mpName, mpRiding, mpParty, mpInfo, billnum_txt, ballot_txt;
    ImageView img;
    Button snsBtn, emailBtn, phoneBtn, subscribeBtn, compareBtn;
    MP mpObj;
    ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);
    ArrayList<Ballot> tempbollotArray = new ArrayList<>(0);
    BallotsAdapter adapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;



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
        billnum_txt = findViewById(R.id.billnum_txt);
        ballot_txt = findViewById(R.id.ballot_txt);
        recyclerView = findViewById(R.id.ballot_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mpName.setText(mpObj.getName());
        mpRiding.setText(mpObj.getRiding());
        mpParty.setText(mpObj.getParty());

        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        networkingService.listener = this;

        networkingService.getImageData(mpObj.getPhotoURL());
        networkingService.fetchMoreMPInfo(mpObj.getName());

    }

    @Override
    public void APINetworkListner(String jsonString) {
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
        //it will take user to twitter home
        networkingService.fetchMPDesc(mpObj.getName());
    }

    @Override
    public void APIBallotListener(String jsonString) {
        allBallotFromMP = jsonService.parseBallots(jsonString);
        for(int i=0; i<allBallotFromMP.size(); i++){
            networkingService.fetchVote(allBallotFromMP.get(i).getVoteURL());
        }

    }

    @Override
    public void APIVoteListener(String jsonString) {
        tempbollotArray.add(jsonService.parseVote(jsonString));
        //list date and bill desc, same size with allBollotFromMP
        if(tempbollotArray.size() == allBallotFromMP.size()){
            for(int i=0; i<allBallotFromMP.size(); i++){
                allBallotFromMP.get(i).setBillNum(tempbollotArray.get(i).getBillNum());
                allBallotFromMP.get(i).setDate(tempbollotArray.get(i).getDate());
            }

            ArrayList<Ballot> shortBallotList = new ArrayList<>(0);
            if(allBallotFromMP.size() > 2){
                for(int j=0; j<2; j++){
                    shortBallotList.add(allBallotFromMP.get(j));
                }
                adapter = new BallotsAdapter(this, shortBallotList);
            }else{
                adapter = new BallotsAdapter(this, allBallotFromMP);
            }

            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }

    @Override
    public void APIMPDescListener(String jsonString) {
        String desc = jsonService.parseMPDesc(jsonString);
        mpInfo.setText(desc);
        networkingService.fetchBallot(mpObj.getBallotURL());
    }

    @Override
    public void APIBillsListener(String jsonString) {

    }

    public void SNSBtnClicked(View view){
        Intent twitterIntent =new Intent("android.intent.action.VIEW",
                Uri.parse("https://twitter.com/"+mpObj.getTwitter()));
        startActivity(twitterIntent);
    }

    public void PhoneBtnClicked(View view){
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
        phoneIntent.setData(Uri.parse("tel:"+mpObj.getPhone()));
        startActivity(phoneIntent);
    }


    public void SeemoreBtnClicked(View view) {
        Intent intent = new Intent(this, BallotListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ballotList", allBallotFromMP);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    public void MPCompareBtnClicked(View view) {
        Intent intent = new Intent(this, CompareMPActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ballotList", allBallotFromMP);
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
                Toast.makeText(this, "Subscribed!", Toast.LENGTH_SHORT).show();
            }
            //if user already followed the MP and wants to unfollow
            else{
                //unfollow and change the text to subscribe
                dbManager.removeMPSubscription(mpName.getText().toString());
                subscribeBtn.setText(R.string.subscribe);
                Toast.makeText(this, "Unfollowed!", Toast.LENGTH_SHORT).show();
            }


            dbManager.setSubObjCallbackInstance(this);
        }
        else{
            //else - not logged in
            Toast.makeText(this, "Login to save MP Subscriptions.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getSub(Subscribed cbReturnSub) {
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
    }

