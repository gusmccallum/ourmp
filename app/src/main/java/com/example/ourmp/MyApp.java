package com.example.ourmp;

import android.app.Application;

public class MyApp extends Application {
    public  NetworkingService getNetworkingService(){
        return networkingService;
    }
    private NetworkingService networkingService = new NetworkingService();
    private JsonService jsonService = new JsonService();

    public  JsonService getJsonService(){
        return jsonService;
    }
}
