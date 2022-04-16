package com.example.ourmp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends BaseActivity {

    Button btn_findMP;
    Button btn_search;
    Button btn_activityFeed;
    Button btn_login;
    Button btn_event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        replaceContentLayout(R.layout.activity_main);

        // Main Activity Buttons

        btn_activityFeed = (Button) findViewById(R.id.MainActivityFeed_btn);
        btn_activityFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityFeedIntent = new Intent();
                activityFeedIntent.setClass(getApplicationContext(), ActivityFeed.class);
                startActivity(activityFeedIntent);
            }
        });

        btn_event = (Button) findViewById(R.id.MainEvents_btn);
        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventIntent = new Intent();
                eventIntent.setClass(getApplicationContext(), Events.class);
                startActivity(eventIntent);
            }
        });

        btn_findMP = (Button) findViewById(R.id.MainFindMP_btn);
        btn_findMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findMPIntent = new Intent();
                findMPIntent.setClass(getApplicationContext(), LocationActivity.class);
                startActivity(findMPIntent);
            }
        });

        btn_search = (Button) findViewById(R.id.MainSearch_btn);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent();
                searchIntent.setClass(getApplicationContext(), Search.class);
                startActivity(searchIntent);
            }
        });


        btn_login = (Button)findViewById(R.id.MainLogIn_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent();
                logInIntent.setClass(getApplicationContext(), LogIn.class);
                startActivity(logInIntent);
            }
        });

        BottomNavigationView botNav = findViewById(R.id.botNav);

        botNav.setSelectedItemId(R.id.home);

        botNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = getIntent();

                if (item.getItemId() == R.id.home) {
                    //Toast.makeText(getApplicationContext(), "Clicked recent events", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(MainActivity.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.search) {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(MainActivity.this, Search.class);
                    startActivity(intent);
                }

                if (item.getItemId() == R.id.events) {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(MainActivity.this, Events.class);
                    startActivity(intent);
                }

                return true;
            }
        });
    }
}