package com.example.ourmp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Subscribed;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends BaseActivity
        implements LocationListener, NetworkingService.NetworkingListener, DBManager.subObjCallback{

    TextView mpName, mpRiding, mpParty;
    Button subscribe_btn;
    NetworkingService networkingService;
    JsonService jsonService;
    MP mpObj = new MP();
    ImageView img;
    RelativeLayout mpCard;
    DBManager dbManager;

    ArrayList<MP> allMPs;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_location);
        replaceContentLayout(R.layout.activity_location);

        //Perform initial query to see if user is subscribed to MP
        if (((MainApplication)getApplication()).getLogInStatus() == true) {
            DBManager dbManager = ((MainApplication)getApplication()).getDbManager();
            dbManager.getSubscriptionObject();
            dbManager.setSubObjCallbackInstance(this);
        }


        mpCard = findViewById(R.id.mpcard_relative);
        //set the MP info card(relative layout) invisible
        mpCard.setVisibility(View.GONE);

        mpName = findViewById(R.id.mpcardName_txt);
        mpRiding = findViewById(R.id.mpcardRiding_txt);
        mpParty = findViewById(R.id.mpcardParty_txt);
        img = findViewById(R.id.mpcard_img);
        subscribe_btn = findViewById(R.id.subscribe_btn);

        //initialize fragment
        Fragment fragment = new MapFragment();
        //call fragment and show it in the frame layout
        getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment).commit();

        //initialize networking service and json service
        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        dbManager = ((MainApplication) getApplication()).getDbManager();
        dbManager.setSubObjCallbackInstance(LocationActivity.this);
        allMPs = ((MainApplication) getApplication()).allMPs;
        networkingService.listener = this;
    }

    //implement interface to get postal from fragment
    @Override
    public void MPCardLocation(Double lat, Double lng) {
        //when we get the location info, fetch api to find mp
        networkingService.fetchMPData(lat, lng);
    }

    @Override
    public void APINetworkListner(String jsonString) {
        //after fetching api, create MP object based on the retrieved info
        mpObj = jsonService.parseFindMPAPI(jsonString);

        //set MP info card visible
        mpCard.setVisibility(View.VISIBLE);
        //bring the card in front of everything
        mpCard.bringToFront();

        //set text view
        mpName.setText(mpObj.getName());
        mpRiding.setText(mpObj.getRiding());
        mpParty.setText(mpObj.getParty());

        //get the image from the image url from the api
        networkingService.getImageData(mpObj.getPhotoURL());
    }

    @Override
    public void APINetworkingListerForImage(Bitmap image) {
        //after gettting image, set the image view with it
        img.setImageBitmap(image);

    }

    @Override
    public void APIMPMoreInfoListener(String jsonString) {

    }

    @Override
    public void APIBallotListener(String jsonString) {

    }

    @Override
    public void APIVoteListener(String jsonString) {

    }

    @Override
    public void APIMPDescListener(String jsonString) {

    }

    @Override
    public void APIBillsListener(String jsonString) {

    }

    @Override
    public void APIMoreBillInfoListener(String jsonString) {

    }


    public void ClosedBtnClicked(View view) {
        //when close button clicked, set the mp card invisible and bring to the back
        mpCard.setVisibility(View.GONE);
        mpCard.invalidate();
    }

    public void SubscribeBtnClicked(View view) {
        //if(check if logged - yes)
        if (((MainApplication)getApplication()).getLogInStatus() == true) {
            DBManager dbManager = ((MainApplication)getApplication()).getDbManager();
            //if the button = subscribe which means user has not followed the MP yet
            if(subscribe_btn.getText().toString().equals(R.string.subscribe)){
                //follow the MP and change the text to unfollow
                dbManager.addMPSubscription(mpName.getText().toString());
                subscribe_btn.setText(R.string.unfollow);
                Toast.makeText(this, "Subscribed!", Toast.LENGTH_SHORT).show();
            }
            //if user already followed the MP and wants to unfollow
            else{
                //unfollow and change the text to subscribe
                dbManager.removeMPSubscription(mpName.getText().toString());
                subscribe_btn.setText(R.string.subscribe);
                Toast.makeText(this, "Unfollowed!", Toast.LENGTH_SHORT).show();
            }


            dbManager.setSubObjCallbackInstance(this);
        }
        else{
            //else - not logged in
            Toast.makeText(this, "Login to save MP Subscriptions.", Toast.LENGTH_SHORT).show();
        }
    }

    public void MoreInfoBtnClicked(View view) {
        Intent intent = new Intent(this, MPCardActivity.class);
        intent.putExtra("selectedMP", mpObj);
        startActivity(intent);
    }

    @Override
    public void getSub(Subscribed cbReturnSub) {
        String MPName = mpName.getText().toString();
        List<String> subscribedMPs = cbReturnSub.getSubscribedMPs();
        if (subscribedMPs != null) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (subscribedMPs.indexOf(MPName) == -1) {
                        subscribe_btn.setText(R.string.subscribe);
                    }
                    else {
                        subscribe_btn.setText(R.string.unfollow);
                    }
                }
            });
        }

    }
}
