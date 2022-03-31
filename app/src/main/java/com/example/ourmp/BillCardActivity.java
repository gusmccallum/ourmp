package com.example.ourmp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Subscribed;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BillCardActivity extends BaseActivity implements NetworkingService.NetworkingListener{
    NetworkingService networkingService;
    JsonService jsonService;
    DBManager dbManager;

    TextView billTitle, billDesc, billDescription;
    TextView billTitle2, billDesc2, billDescription2;

    Activity activity;
    Bill bill = null, comparedBill = null;

    Button compareBtn;
    ArrayList<PartyVote> partyVotes = new ArrayList<>();
    RelativeLayout comparedBillView;
    RelativeLayout noVoteView;

    TextView conservativeVote;
    TextView liberalVote;
    TextView ndpVote;
    TextView greenVote;

    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_billcard);

        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        networkingService.listener = this;

        activity = getIntent().getParcelableExtra("bill");

        networkingService.fetchMoreBillInfo(activity.url);

        billTitle = findViewById(R.id.bill_number);
        billDesc = findViewById(R.id.bill_desc);
        billDescription = findViewById(R.id.bill_description);
        billTitle2 = findViewById(R.id.bill_number2);
        billDesc2 = findViewById(R.id.bill_desc2);
        billDescription2 = findViewById(R.id.bill_description2);
        compareBtn = findViewById(R.id.bill_compare_btn);
        comparedBillView = findViewById(R.id.comparedBill);
        noVoteView = findViewById(R.id.novotes);

        conservativeVote = findViewById(R.id.conservative);
        liberalVote = findViewById(R.id.liberal);
        ndpVote = findViewById(R.id.ndp);
        greenVote = findViewById(R.id.green);
        progressBar = findViewById(R.id.progressBar);


    }

    @Override
    public void APINetworkListner(String jsonString) {

    }

    @Override
    public void APINetworkingListerForImage(Bitmap image) {

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
        bill = jsonService.parseMoreBillInfo(jsonString);
        billTitle.setText("Bill Number: " + bill.getBillNum());
        String description = "Session: " + bill.getBillSession() + "\nStatus: " + bill.getBillResult() + "\nDate: " + bill.getBillDate();
        billDesc.setText(description);
        billDescription.setText("Description: " + bill.getBillDesc());
        if(bill.getVoteURl() != "") {
            networkingService.fetchBillVotes(bill.getVoteURl());
        }

    }

    @Override
    public void APIParseBillVote(String jsonString) {
        partyVotes = jsonService.parseBillVote(jsonString);
        int countYes = 0;
        for(int i=0; i<partyVotes.size(); i++){
            if(partyVotes.get(i).name.equals("Conservative")){
                conservativeVote.setText(partyVotes.get(i).vote);
            }else if(partyVotes.get(i).name.equals("Green")){
                greenVote.setText(partyVotes.get(i).vote);
            }else if(partyVotes.get(i).name.equals("NDP")){
                ndpVote.setText(partyVotes.get(i).vote);
            }else if(partyVotes.get(i).name.equals("Liberal")){
                liberalVote.setText(partyVotes.get(i).vote);
            }
            if(partyVotes.get(i).vote.equals("Yes")){
                countYes+=20;
            }

        }

        progressBar.setProgress(countYes);
    }

    public void clickCompareButton(View view){
        if(bill.getVoteURl() == ""){
            noVoteView.setVisibility(View.VISIBLE);
        }else {
            comparedBillView.setVisibility(View.VISIBLE);
        }
    }
}
