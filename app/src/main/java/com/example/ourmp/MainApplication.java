package com.example.ourmp;

import static io.realm.Realm.getApplicationContext;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainApplication extends Application {

    private String appID = "ourmp-ksaww";
    private App app;
    private boolean isLoggedIn = false;
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

    public void setLogInStatus(boolean status) {
        isLoggedIn = status;
    }

    public boolean getLogInStatus() {
        return isLoggedIn;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        // Initialize Amplify

        try {
            Amplify.addPlugin(new AWSApiPlugin()); // UNCOMMENT this line once backend is deployed
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Amplify", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("Amplify", "Could not initialize Amplify", error);
        }
        app = new App(new AppConfiguration.Builder(appID).build());


    }
}
