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

    Button btn_findMP;
    Button btn_search;
    Button btn_activityFeed;
    Button btn_signup;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Realm.init(this);
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

        btn_activityFeed = (Button)findViewById(R.id.MainActivityFeed_btn);
        btn_activityFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityFeedIntent = new Intent();
                activityFeedIntent.setClass(getApplicationContext(), ActivityFeed.class);
                startActivity(activityFeedIntent);
            }
        });

        btn_signup = (Button)findViewById(R.id.MainSignUp_btn);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent();
                signUpIntent.setClass(getApplicationContext(), SignUp.class);
                startActivity(signUpIntent);
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