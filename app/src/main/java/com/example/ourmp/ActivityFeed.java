package com.example.ourmp;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityFeed extends AppCompatActivity implements NetworkingService.NetworkingListener {

    NetworkingService networkingService;
    JsonService jsonService;
    ListView activityList;
    ArrayList<Activity> bills = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //initialize networking service and json service
        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        networkingService.fetchBillsData();

        activityList = findViewById(R.id.activityList);
        networkingService.listener = this;
    }

    @Override
    public void APINetworkListner(String jsonString) {
        //after fetching api, create MP object based on the retrieved info
        bills = jsonService.parseFindBills(jsonString);

        ActivityFeedBaseAdapter adapter = new ActivityFeedBaseAdapter(bills, this);
        activityList.setAdapter(adapter);
    }

    @Override
    public void APINetworkingListerForImage(Bitmap image) {

    }
}
