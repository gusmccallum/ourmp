package com.example.ourmp;

import static io.realm.Realm.getApplicationContext;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

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

    public ArrayList<MPID> allMPIds = new ArrayList<MPID>();



    public String getMPId(String mpName) {
        for (int i = 0; i < allMPIds.size(); i++) {
            if (allMPIds.get(i).getMPName().equals(mpName)) {
                return allMPIds.get(i).getMPID();
            }
        }
        return "";
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

    public ArrayList<String> searchHistoryMPs = new ArrayList<>();

    public void setLogInStatus(boolean status) {
        isLoggedIn = status;
    }

    public boolean getLogInStatus() {
        return isLoggedIn;
    }

    public void logOut() {
        app.currentUser().logOutAsync(it -> {
            if (it.isSuccess()) {
                Toast.makeText(getApplicationContext(), "Logged Out.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Error logging out: " + it.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        isLoggedIn = false;
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

        /*
        Amplify.DataStore.clear(
                () -> Log.i("OurMP", "DataStore is cleared."),
                failure -> Log.e("OurMP", "Failed to clear DataStore.")); */



    }
}
