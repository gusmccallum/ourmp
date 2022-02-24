package com.example.ourmp;


import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.util.Log;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class DBManager {
    String appID = "ourmp-ksaww";
    App app;
    User user; // realm user not object
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    CodecRegistry pojoCodecRegistry;
    MongoCollection<Subscribed> subscribedCollection;

    public Realm getRealmInstance(){

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(appID)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .compactOnLaunch()
                .build();

        return Realm.getInstance(config);

    }
    public void atlasConfig(){
        app = new App(new AppConfiguration.Builder(appID)
                .appName("OurMP")
                .requestTimeout(30, TimeUnit.SECONDS)
                .build());;

        Credentials anonymousCredentials = Credentials.anonymous();
        AtomicReference<User> anonymousUser = new AtomicReference<User>();
        app.loginAsync(anonymousCredentials, it -> {
            if (it.isSuccess()) {
                Log.v("AUTH", "Successfully authenticated anonymously.");
                anonymousUser.set(app.currentUser());
            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });

        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("ourmpdb");
        pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        subscribedCollection = mongoDatabase.getCollection(
                        "subscribed",
                        Subscribed.class).withCodecRegistry(pojoCodecRegistry);
    }

    //insert MP in db
    public void insertMPs(String id, String mps){
        Realm realm = getRealmInstance();
        atlasConfig();

        Document queryFilter  = new Document("userId", id);
        //find if the user with id has subscription

        subscribedCollection.findOne(queryFilter).getAsync(task -> {
            if (task.isSuccess()) {
                addMPSub(mps);
            }
            else{
                Subscribed newSub = new Subscribed(id);
                newSub.addMPs(mps);
                subscribedCollection.insertOne(newSub);
            }
        });

        realm.close();

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
        //check if MPs attribute has the same name
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
