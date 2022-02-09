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

import static io.realm.Realm.getApplicationContext;

public class DBManager {
    String appID = "ourmp-ksaww";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    User user; // realm user not object
    //MongoCollection<Document> mongoCollection = new MongoCollection<Document>(1);

    public void DBManager(){
        Realm.init(getApplicationContext());
        app = new App(new AppConfiguration.Builder(appID).build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ourmpdb");
    }

    //insert MP in db
    public void insertMPs(int id, String mps){
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("subscribed");
        //find if the user with id has subscribe
        /*Document queryFilter  = new Document("userId", id);
        //find userId with same value with id
        mongoCollection.findOne(queryFilter).getAsync(task -> {
            if (task.isSuccess()) { //user has sub -> update the data
                Document result = task.get();
                Log.v("EXAMPLE", ""+result);
            } else {//doesn have -> insert
                Log.e("EXAMPLE", "fail: "+task.getError());
            }
        });*/
    }

    //need to implement
    public void insertBills(int id, ArrayList legislations){
        //mongoCollection = mongoDatabase.getCollection("subscribed");

    }
    //need to implement
    public void insertUsers(){
        //mongoCollection = mongoDatabase.getCollection("users");
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
