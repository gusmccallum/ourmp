package com.example.ourmp;

import static io.realm.Realm.getApplicationContext;

import android.app.Application;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainApplication extends Application {

    private String appID = "ourmp-ksaww";
    private App app;
    public App getRealmApp() {
        return app;
    }

    public ArrayList<MP> allMPs = new ArrayList<>();
    private DBManager dbManager = new DBManager();
    public DBManager getDbManager(){return dbManager;}

    public  NetworkingService getNetworkingService(){
        return networkingService;
    }
    private NetworkingService networkingService = new NetworkingService();

    private JsonService jsonService = new JsonService();
    public  JsonService getJsonService() {
        return jsonService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(appID).build());
    }
}
