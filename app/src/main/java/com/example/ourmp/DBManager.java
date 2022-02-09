package com.example.ourmp;

import static io.realm.Realm.getApplicationContext;


import android.util.Log;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class DBManager {
    String Appid = "ourmp-ksaww";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    User user;
    MongoCollection<Document> mongoCollection;

    public void DBManager(){

        //Initialize Realm
        Realm.init(getApplicationContext());
        app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ourmpdb");


        //Log user in anonymously
        app.loginAsync(Credentials.anonymous(), new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if (result.isSuccess())
                {
                    Log.v("User", "Logged in anonymously");

                }
                else {
                    Log.v("User", "Failed to login");
                }
            }
        });
    }

    //Allow current user to subscribe to new MP
    private void addMPSub(String MPName) {
        //TODO
    }

    //Allow current user to subscribe to new Bill
    private void addBillSub(String billID) {
        //TODO
    }

    //Set all new MP subscriptions for current user
    private void updateMPSubs(ArrayList<String> MPNames) {
        //TODO
    }

    //Set all new Bill subscriptions for current user
    private void updateBillSubs(ArrayList<String> billIDs) {
        //TODO
    }

    //Set all new subscriptions for current user
    // (migration from anonymous to signed in user on signup)
    private void updateAllSubs(ArrayList<String> MPNames, ArrayList<String> billIDs) {
        //TODO
    }

    //Unsubscribe current user from MP
    private void removeMPSub(String MPName) {
        //TODO
    }

    //Unsubscribe current user from Bill
    private void removeBillSubs(String billID) {
        //TODO
    }





}
