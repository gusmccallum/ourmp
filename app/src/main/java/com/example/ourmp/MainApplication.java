package com.example.ourmp;

import android.app.Application;
import android.util.Log;


//application level subclass to initialize realm on app startup
public class MainApplication extends Application {


    //Sign in users anonymously until this is true, delete anonymous account if and when they make an account and sign in
    final boolean signedIn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        DBManager DB = new DBManager();

    }
}
