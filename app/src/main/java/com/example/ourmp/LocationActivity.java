package com.example.ourmp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class LocationActivity extends AppCompatActivity
        implements LocationListener, NetworkingService.NetworkingListener{

    TextView mpName, mpRiding, mpParty;
    NetworkingService networkingService;
    JsonService jsonService;
    MP mpObj = new MP();
    ImageView img;
    RelativeLayout mpCard;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mpCard = findViewById(R.id.mpcard_relative);
        //set the MP info card(relative layout) invisible
        mpCard.setVisibility(View.GONE);

        mpName = findViewById(R.id.mpcardName_txt);
        mpRiding = findViewById(R.id.mpcardRiding_txt);
        mpParty = findViewById(R.id.mpcardParty_txt);
        img = findViewById(R.id.mpcard_img);

        //initialize fragment
        Fragment fragment = new MapFragment();
        //call fragment and show it in the frame layout
        getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment).commit();

        //initialize networking service and json service
        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        dbManager = ((MainApplication) getApplication()).getDbManager();
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


    public void ClosedBtnClicked(View view) {
        //when close button clicked, set the mp card invisible and bring to the back
        mpCard.setVisibility(View.GONE);
        mpCard.invalidate();
    }

    public void SubscribeBtnClicked(View view) {
        //if(check if logged - yes)
        //id will be riding+name?
        //dbManager.insertMPs("6", mpName.getText().toString());
        Toast.makeText(this, "Subscribed!", Toast.LENGTH_SHORT).show();
        //else - not logged in
        //Toast.makeText(this, "Need to login to subscribe MP!", Toast.LENGTH_SHORT).show();
    }

    public void MoreInfoBtnClicked(View view) {
        Intent intent = new Intent(this, MPCardActivity.class);
        intent.putExtra("selectedMP", mpObj);
        startActivity(intent);
    }
}
