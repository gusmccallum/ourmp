package com.example.ourmp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CompareMPActivity extends AppCompatActivity
        implements NetworkingService.NetworkingListener{

    NetworkingService networkingService;
    JsonService jsonService;
    ArrayList<Ballot> allBallotFromMP1 = new ArrayList<>(0);
    ArrayList<Ballot> allBallotFromMP2 = new ArrayList<>(0);
    MP mpObj1;
    ImageView mp1_img, mp2_img;
    TextView mp1_name, mp2_name;
    MP1CompareAdapter adapter1, adapter2;
    RecyclerView recyclerView1, recyclerView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_mp);

        Bundle bundleFromMainActivity = getIntent().getBundleExtra("bundle");
        allBallotFromMP1 = bundleFromMainActivity.getParcelableArrayList("ballotList");
        mpObj1 = getIntent().getParcelableExtra("MPObj");

        mp1_img = findViewById(R.id.mp1_img);
        mp2_img = findViewById(R.id.mp2_img);
        mp1_name = findViewById(R.id.mp1_name_txt);
        mp2_name = findViewById(R.id.mp2_name_txt);

        recyclerView1 = findViewById(R.id.mp1_list);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2 = findViewById(R.id.mp2_list);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        networkingService.listener = this;

        networkingService.getImageData(mpObj1.getPhotoURL());
        mp1_name.setText(mpObj1.getName());
        adapter1 = new MP1CompareAdapter(this, allBallotFromMP1, 1);//enter 1 for MP1
        recyclerView1.setAdapter(adapter1);
    }

    @Override
    public void APINetworkListner(String jsonString) {

    }

    @Override
    public void APINetworkingListerForImage(Bitmap image) {
        mp1_img.setImageBitmap(image);
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
}
