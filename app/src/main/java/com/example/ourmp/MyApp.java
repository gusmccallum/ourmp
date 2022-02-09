package com.example.ourmp;

import android.app.Application;

public class MyApp extends Application {

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
