package com.example.ourmp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import io.realm.Realm;

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
        mpCard = (RelativeLayout) findViewById(R.id.mpcard_relative);
        //set the MP info card(relative layout) invisible
        mpCard.setVisibility(View.GONE);

        mpName = (TextView) findViewById(R.id.mpcardName_txt);
        mpRiding = (TextView) findViewById(R.id.mpcardRiding_txt);
        mpParty = (TextView) findViewById(R.id.mpcardParty_txt);
        img = (ImageView) findViewById(R.id.mpcard_img);

        //initialize fragment
        Fragment fragment = new MapFragment();
        //call fragment and show it in the frame layout
        getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment).commit();

        //initialize networking service and json service
        networkingService = ( (MyApp)getApplication()).getNetworkingService();
        jsonService = ( (MyApp)getApplication()).getJsonService();
        dbManager = ((MyApp) getApplication()).getDbManager();
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
        mpRiding.setText("Riding: " + mpObj.getRiding());
        mpParty.setText("Party: " + mpObj.getParty());

        //get the image from the image url from the api
        networkingService.getImageData(mpObj.getPhotoURL());
    }

    @Override
    public void APINetworkingListerForImage(Bitmap image) {
        //after gettting image, set the image view with it
        img.setImageBitmap(image);

    }

    public void ClosedBtnClicked(View view) {
        //when close button clicked, set the mp card invisible and bring to the back
        mpCard.setVisibility(View.GONE);
        mpCard.invalidate();
    }

    public void SubscribeBtnClicked(View view) {
        //if(check if logged - yes)
        dbManager.insertMPs(1, mpName.getText().toString());
        Toast.makeText(this, "Subscribed!", Toast.LENGTH_SHORT).show();
        //else - not logged in
        //Toast.makeText(this, "Need to login to subscribe MP!", Toast.LENGTH_SHORT).show();
    }

    public void MoreInfoBtnClicked(View view) {
        //need to start MP info page passing MP info
        Toast.makeText(this, "It will show MP page", Toast.LENGTH_SHORT).show();
    }
}
