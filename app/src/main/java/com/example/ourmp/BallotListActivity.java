package com.example.ourmp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

        BottomNavigationView botNav = findViewById(R.id.botNav);

        botNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Intent intent = getIntent();

                if (item.getItemId() == R.id.home)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked recent events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BallotListActivity.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.search)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BallotListActivity.this, Search.class);
                }

                if (item.getItemId() == R.id.events)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BallotListActivity.this, Events.class);
                }

                startActivity(intent);
                return true;
            }
        });
    }
}
