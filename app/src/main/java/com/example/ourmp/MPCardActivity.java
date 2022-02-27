package com.example.ourmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MPCardActivity extends AppCompatActivity
        implements NetworkingService.NetworkingListener{

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpcard);

        mpObj = getIntent().getParcelableExtra("selectedMP");

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
        }
    }

    @Override
    public void APIMPDescListener(String jsonString) {
        String desc = jsonService.parseMPDesc(jsonString);
        mpInfo.setText(desc);
        networkingService.fetchBallot(mpObj.getBallotURL());
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
}