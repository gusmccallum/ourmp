package com.example.ourmp;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityFeed extends BaseActivity implements NetworkingService.NetworkingListener {

    NetworkingService networkingService;
    JsonService jsonService;
    RecyclerView activityList;
    ArrayList<Activity> activities = new ArrayList<>();

    ArrayList<MP> allMPs;

    MP mpObj;
    ActivityFeedBaseAdapter adapter;
    ActivityFeedRecyclerAdapter recyclerAdapter;
    ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);
    ArrayList<Ballot> tempbollotArray = new ArrayList<>(0);
    int currentMP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_feed);
        replaceContentLayout(R.layout.activity_feed);

        //initialize networking service and json service
        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        networkingService.listener = this;
        allMPs = ((MainApplication) getApplication()).allMPs;

        activityList = findViewById(R.id.activityList);
        recyclerAdapter = new ActivityFeedRecyclerAdapter(activities, this);
        activityList.setAdapter(recyclerAdapter);
        activityList.setLayoutManager(new LinearLayoutManager(this));
        networkingService.fetchBillsData();

        for(int i=0; i<allMPs.size();i++) {
            currentMP = i;
            networkingService.fetchMoreMPInfo(allMPs.get(currentMP).getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        allMPs = ((MainApplication) getApplication()).allMPs;
    }

    @Override
    public void APINetworkListner(String jsonString) {
    }

    @Override
    public void APIBillsListener(String jsonString){
        ArrayList<Activity> temp = new ArrayList<>();
        temp = jsonService.parseFindBills(jsonString);
        activities.addAll(temp);
        activities.sort(Comparator.comparing(obj -> obj.activityDate));
        Collections.reverse(activities);
        recyclerAdapter.notifyDataSetChanged();
        //adapter = new ActivityFeedBaseAdapter(activities, this);
        //activityList.setAdapter(adapter);

    }


    @Override
    public void APINetworkingListerForImage(Bitmap image) {
    }

    @Override
    public void APIMPMoreInfoListener(String jsonString) {
        MP tempMp = jsonService.parseMoreInfoAPI(jsonString);
        allMPs.get(currentMP).setBallotURL(tempMp.getBallotURL());
        new DownloadImage(allMPs.get(currentMP)).execute(allMPs.get(currentMP).getPhotoURL());
        networkingService.fetchBallot(allMPs.get(currentMP).getBallotURL());
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
                activities.add(new Activity(allMPs.get(currentMP).getPhoto(), "MP " + allMPs.get(currentMP).getName(), "Voted " + allBallotFromMP.get(i).getBallot() + " on " + allBallotFromMP.get(i).getBillNum(), allBallotFromMP.get(i).getDate()));
            }
            tempbollotArray.clear();
            allBallotFromMP.clear();
            activities.sort(Comparator.comparing(obj -> obj.activityDate));
            Collections.reverse(activities);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void APIMPDescListener(String jsonString) {
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
}