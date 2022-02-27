package com.example.ourmp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BallotListActivity extends AppCompatActivity {
    ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);
    BallotsAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ballot_list);

        recyclerView = findViewById(R.id.ballots_list);

        Bundle bundleFromMainActivity = getIntent().getBundleExtra("bundle");
        allBallotFromMP = bundleFromMainActivity.getParcelableArrayList("ballotList");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BallotsAdapter(this, allBallotFromMP);
        recyclerView.setAdapter(adapter);
    }
}
