package com.example.ourmp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends BaseActivity {

    Button btn_findMP;
    Button btn_search;
    Button btn_activityFeed;
    Button btn_signup;
    Button btn_login;
    Button btn_event;
    Button btn_create;
    Button btn_read;
    Button btn_update;
    Button btn_delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        replaceContentLayout(R.layout.activity_main);



        // Main Activity Buttons

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


    }
}