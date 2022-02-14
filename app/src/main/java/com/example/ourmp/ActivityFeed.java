package com.example.ourmp;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityFeed extends AppCompatActivity {

    //NetworkingService networkingService;
    //JsonService jsonService;
    ListView activityList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        activityList = findViewById(R.id.activityList);
        ArrayList<Activity> arrayList = new ArrayList<Activity>();
        arrayList.add(new Activity("", "JAVA", "10/2/2021"));
        arrayList.add(new Activity("", "JAVA", "10/2/2021"));
        arrayList.add(new Activity("", "JAVA", "10/2/2021"));

        ActivityFeedBaseAdapter adapter = new ActivityFeedBaseAdapter(arrayList, this);
        activityList.setAdapter(adapter);

    }
}
