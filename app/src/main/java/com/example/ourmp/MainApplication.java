package com.example.ourmp;

import android.app.Application;

import io.realm.Realm;

public class MainApplication extends Application {

    Realm.init(this);


    private DBManager dbManager = new DBManager();
    public DBManager getDbManager(){return dbManager;}

    public  NetworkingService getNetworkingService(){
        return networkingService;
    }
    private NetworkingService networkingService = new NetworkingService();
    private JsonService jsonService = new JsonService();

    public  JsonService getJsonService(){
        return jsonService;
    }
}
