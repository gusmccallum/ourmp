package com.example.ourmp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    Button findMP;
    Button btn_search;
    Button btn_activityFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Realm.init(this);
        findMP = (Button) findViewById(R.id.findMP);
        findMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(), LocationActivity.class);
                startActivity(myIntent);
            }
        });

        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

        btn_activityFeed = (Button)findViewById(R.id.btn_activityFeed);
        btn_activityFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.setClass(getApplicationContext(), ActivityFeed.class);
                startActivity(intent2);
            }
        });
    }
}