package com.example.ourmp;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Subscribed2;
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

import java.io.InputStream;
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
    List<String> subscribedMPs;
    MP mpObj;
    int currentMP = 0;
    ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);
    ArrayList<Ballot> tempbollotArray = new ArrayList<>(0);
    ArrayList<Ballot> validBollotList = new ArrayList<>(0);
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_feed);

        //check if user log in or not
        if (((MainApplication)getApplication()).getLogInStatus() == true) {
            dbManager = ((MainApplication)getApplication()).getDbManager();
            dbManager.getSubscriptionObject();
            dbManager.setSubObjCallbackInstance(this);
/*
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
*/

            //initialize views
            activityList = findViewById(R.id.recyclerView_Bills);
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
    public void APINetworkListener(String jsonString) {
    }

    @Override
    public void APIBillsListener(String jsonString){
        ArrayList<Activity> temp = new ArrayList<>();
        temp = jsonService.parseFindBills(jsonString);
        activities.addAll(temp);
        activities.sort(Comparator.comparing(obj -> obj.activityDate));
        Collections.reverse(activities);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void APIMoreBillInfoListener(String jsonString) {
        Bill temp = jsonService.parseMoreBillInfo(jsonString);
        activities.add(new Activity(null, "Bill " + temp.getBillNum() + " is " + temp.getBillResult(), temp.getBillDesc(), temp.getBillDate(), ""));
        recyclerAdapter.notifyDataSetChanged();
        if (subscribedMPs != null) {
            for (int i = 0; i < subscribedMPs.size(); i++) {
                currentMP = i;
                networkingService.fetchMoreMPInfo(subscribedMPs.get(i));
            }
        }
        //progressDialog.dismiss();
    }

    @Override
    public void APIParseBillVote(String jsonString) {

    }

    @Override
    public void APINetworkingListerForImage2(Bitmap image) {

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
        //VolleyFetchBallotAPI();
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

            for(int i=0; i<allBallotFromMP.size(); i++) {
                if (!allBallotFromMP.get(i).getBillNum().equals("null")) {
                    activities.add(new Activity(allMPs.get(currentMP).getPhoto(), "MP " + allMPs.get(currentMP).getName(), "Voted " + allBallotFromMP.get(i).getBallot() + " on " + allBallotFromMP.get(i).getBillNum(), allBallotFromMP.get(i).getDate(), ""));
                }
            }
            tempbollotArray.clear();
            allBallotFromMP.clear();
            activities.sort(Comparator.comparing(obj -> obj.activityDate));
            Collections.reverse(activities);
            recyclerAdapter.notifyDataSetChanged();
            //progressDialog.dismiss();
        }
    }

    @Override
    public void APIMPDescListener(String jsonString) {
    }

    @Override
    public void getSub(Subscribed2 cbReturnSub) {
        subscribedMPs = cbReturnSub.getSubscribedMPs();
        List<String> subscribedBills = cbReturnSub.getSubscribedBills();
        if (subscribedBills != null){
            for(int i = 0; i < subscribedBills.size(); i++){
                networkingService.fetchMoreBillInfo(subscribedBills.get(i));
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

}