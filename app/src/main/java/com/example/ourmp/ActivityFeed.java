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

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Subscribed;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityFeed extends BaseActivity implements NetworkingService.NetworkingListener, DBManager.subObjCallback {

    //Services
    NetworkingService networkingService;
    JsonService jsonService;
    DBManager dbManager;

    //Views
    RecyclerView activityList;
    ActivityFeedBaseAdapter adapter;
    ActivityFeedRecyclerAdapter recyclerAdapter;

    //Variables holding data
    ArrayList<Activity> activities = new ArrayList<>();
    ArrayList<MP> allMPs;
    ArrayList<String> mpNames;
    MP mpObj;
    int currentMP = 0;
    ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);
    ArrayList<Ballot> tempbollotArray = new ArrayList<>(0);


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_feed);

        //check if user log in or not
        if (((MainApplication)getApplication()).getLogInStatus() == true) {
            dbManager = ((MainApplication)getApplication()).getDbManager();
            dbManager.getSubscriptionObject();
            dbManager.setSubObjCallbackInstance(this);


            //initialize views
            activityList = findViewById(R.id.activityList);
            recyclerAdapter = new ActivityFeedRecyclerAdapter(activities, this);
            activityList.setAdapter(recyclerAdapter);
            activityList.setLayoutManager(new LinearLayoutManager(this));

            //initialize networking service and json service
            networkingService = ( (MainApplication)getApplication()).getNetworkingService();
            jsonService = ( (MainApplication)getApplication()).getJsonService();
            networkingService.listener = this;

        }else{
            Toast.makeText(this, "Sign in to view Activity Feed", Toast.LENGTH_SHORT).show();
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
    public void APIMoreBillInfoListener(String jsonString) {
        Bill temp = jsonService.parseMoreBillInfo(jsonString);
        activities.add(new Activity(null, "Bill " + temp.getBillNum() + " is " + temp.getBillResult(), temp.getBillDesc(), temp.getBillDate(), ""));
    }

    @Override
    public void APIParseBillVote(String jsonString) {

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
        }catch(JSONException e){
            e.printStackTrace();
        }

        allMPs.add(tempMp);
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
                activities.add(new Activity(allMPs.get(currentMP).getPhoto(), "MP " + allMPs.get(currentMP).getName(), "Voted " + allBallotFromMP.get(i).getBallot() + " on " + allBallotFromMP.get(i).getBillNum(), allBallotFromMP.get(i).getDate(), ""));
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

    @Override
    public void getSub(Subscribed cbReturnSub) {
        List<String> subscribedMPs = cbReturnSub.getSubscribedMPs();
        if (subscribedMPs != null) {
            for (int i = 0; i < subscribedMPs.size(); i++) {
                currentMP = i;
                networkingService.fetchMoreMPInfo(subscribedMPs.get(i));
            }
        }

        List<String> subscribedBills = cbReturnSub.getSubscribedBills();
        if (subscribedBills != null){
            for(int i = 0; i < subscribedBills.size(); i++){
                networkingService.fetchMoreBillInfo(subscribedBills.get(i));
            }
        }

        activities.sort(Comparator.comparing(obj -> obj.activityDate));
        Collections.reverse(activities);
        recyclerAdapter.notifyDataSetChanged();
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