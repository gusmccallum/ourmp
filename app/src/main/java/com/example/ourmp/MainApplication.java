package com.example.ourmp;

import static io.realm.Realm.getApplicationContext;

import android.app.Application;

import io.realm.Realm;

public class MainApplication extends Application {

<<<<<<< Updated upstream
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
=======
    private DBManager dbManager = new DBManager();
    public DBManager getDbManager(){return dbManager;}

    private NetworkingService networkingService = new NetworkingService();
    public  NetworkingService getNetworkingService(){
        return networkingService;
    }

    private JsonService jsonService = new JsonService();
    public  JsonService getJsonService(){
        return jsonService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
>>>>>>> Stashed changes
    }
}
