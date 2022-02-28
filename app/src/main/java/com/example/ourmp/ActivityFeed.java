package com.example.ourmp;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityFeed extends AppCompatActivity implements NetworkingService.NetworkingListener {

    NetworkingService networkingService;
    JsonService jsonService;
    ListView activityList;
    ArrayList<Activity> activities = new ArrayList<>();

    ArrayList<MP> allMPs;

    MP mpObj;
    ActivityFeedBaseAdapter adapter;
    ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);
    ArrayList<Ballot> tempbollotArray = new ArrayList<>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //initialize networking service and json service
        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        networkingService.listener = this;
        allMPs = ((MainApplication) getApplication()).allMPs;
        mpObj = allMPs.get(0);
        activityList = findViewById(R.id.activityList);
        networkingService.getImageData(mpObj.getPhotoURL());

        networkingService.fetchBillsData();




    }

    @Override
    public void APINetworkListner(String jsonString) {
    }

    @Override
    public void APIBillsListener(String jsonString){
        ArrayList<Activity> temp = new ArrayList<>();
        temp = jsonService.parseFindBills(jsonString);
        activities.addAll(temp);
        networkingService.fetchMoreMPInfo(mpObj.getName());
    }

    @Override
    public void APINetworkingListerForImage(Bitmap image) {
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
        if(tempbollotArray.size() == allBallotFromMP.size()) {
            for (int i = 0; i < allBallotFromMP.size(); i++) {
                allBallotFromMP.get(i).setBillNum(tempbollotArray.get(i).getBillNum());
                allBallotFromMP.get(i).setDate(tempbollotArray.get(i).getDate());
            }

            for(int i=0; i<allBallotFromMP.size(); i++){
                activities.add(new Activity(mpObj.getPhoto(), "MP " + mpObj.getName(), "Voted " + allBallotFromMP.get(i).getBallot() + " on " + allBallotFromMP.get(i).getBillNum(), allBallotFromMP.get(i).getDate()));
            }

            activities.sort(Comparator.comparing(obj -> obj.activityDate));
            Collections.reverse(activities);
            adapter = new ActivityFeedBaseAdapter(activities, this);
            activityList.setAdapter(adapter);
        }
    }

    @Override
    public void APIMPDescListener(String jsonString) {
        networkingService.fetchBallot(mpObj.getBallotURL());
    }
}
