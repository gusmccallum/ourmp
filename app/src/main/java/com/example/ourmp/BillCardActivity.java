package com.example.ourmp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Subscribed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BillCardActivity extends BaseActivity implements NetworkingService.NetworkingListener, BillFeedRecyclerAdapter.OnItemClickListener {
    NetworkingService networkingService;
    JsonService jsonService;
    DBManager dbManager;

    TextView billTitle, billDesc, billDescription;
    TextView billTitle2, billDesc2, billDescription2;

    Activity activity;
    Bill bill = null, comparedBill = null;

    Button compareBtn;
    CardView searchBar;
    RecyclerView results;
    EditText searchText;
    ArrayList<Activity> activities = new ArrayList<>();
    BillFeedRecyclerAdapter recyclerAdapter;
    RelativeLayout comparedBillView;

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
        searchBar = findViewById(R.id.search_bar);
        comparedBillView = findViewById(R.id.comparedBill);

        results = findViewById(R.id.billsList);
        recyclerAdapter = new BillFeedRecyclerAdapter(activities, this);
        results.setAdapter(recyclerAdapter);
        results.setLayoutManager(new LinearLayoutManager(this));

        searchText = findViewById(R.id.search_bill_input);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterQuery(editable.toString());
            }
        });
    }

    public void filterQuery(String text) {
        if (!text.isEmpty()) {
            ArrayList<Activity> filteredBills = new ArrayList<>();
            results.setVisibility(View.VISIBLE);
            for (Activity bill : this.activities) {
                if (bill.activityTitle.toLowerCase().contains(text.toLowerCase())) {
                    filteredBills.add(bill);
                    recyclerAdapter.update();
                }
            }
            this.recyclerAdapter.setFilter(filteredBills);
        } else {
            results.setVisibility(View.GONE);
        }
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
        ArrayList<Activity> temp = new ArrayList<>();
        temp = jsonService.parseFindBills(jsonString);
        activities.addAll(temp);
        activities.sort(Comparator.comparing(obj -> obj.activityDate));
        Collections.reverse(activities);
        recyclerAdapter.notifyDataSetChanged();
        //adapter = new ActivityFeedBaseAdapter(activities, this);
        //activityList.setAdapter(adapter);
    }

    @Override
    public void APIMoreBillInfoListener(String jsonString) {
        if(bill == null) {
            bill = jsonService.parseMoreBillInfo(jsonString);
            billTitle.setText("Bill Number: " + bill.getBillNum());
            String description = "Session: " + bill.getBillSession() + "\nStatus: " + bill.getBillResult() + "\nDate: " + bill.getBillDate();
            billDesc.setText(description);
            billDescription.setText("Description: " + bill.getBillDesc());
            networkingService.fetchBillsData();
        }else{
            comparedBill = jsonService.parseMoreBillInfo(jsonString);
            billTitle2.setText("Bill Number: " + comparedBill.getBillNum());
            String description = "Session: " + comparedBill.getBillSession() + "\nStatus: " + comparedBill.getBillResult() + "\nDate: " + comparedBill.getBillDate();
            billDesc2.setText(description);
            billDescription2.setText("Description: " + comparedBill.getBillDesc());
        }
    }

    public void clickCompareButton(View view){
        searchBar.setVisibility(View.VISIBLE);
        comparedBillView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(Activity bill) {
        searchBar.setVisibility(View.INVISIBLE);
        results.setVisibility(View.INVISIBLE);
        networkingService.fetchMoreBillInfo(bill.url);
        comparedBillView.setVisibility(View.VISIBLE);
    }
}
