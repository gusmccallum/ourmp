package com.example.ourmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Events extends BaseActivity
{
    List<Bill> billList;
    RecyclerView recyclerView;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_third);

        replaceContentLayout(R.layout.activity_events);
        recyclerView = findViewById(R.id.recyclerView);
        billList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        initData();
        initRecyclerView();

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
                    intent = new Intent(Events.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.search)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Events.this, Search.class);
                }

                if (item.getItemId() == R.id.events)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Events.this, Events.class);
                }

                startActivity(intent);
                return true;
            }
        });
    }

    private void initRecyclerView()
    {
        MyAdapter myAdapter = new MyAdapter(billList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData()
    {
        //String url = "https://api.openparliament.ca/votes/?session=44-1&format=json";
        String url = "https://www.ourcommons.ca/members/en/votes/csv";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    String lines[] = response.split("\\r?\\n");

                    for (int i = 1; i < lines.length - 1; i++) {
                        String[] temp = lines[i].split(",");
                        if (!temp[temp.length - 1].equals("0")) {

                            String billNum = temp[temp.length - 1];
                            String billSession = "44-1";
                            String billDate = temp[2];
                            String billResult = temp[temp.length-5];
                            String billDesc;
                            if (temp.length == 11) {
                                billDesc = temp[4] + temp[5];
                            }
                            else {
                                billDesc = temp[4] + temp[5] + temp[6];
                            }
                            String yesVotes = temp[temp.length-4];
                            String noVotes = temp[temp.length-3];
                            billList.add(new Bill(billNum, billSession, billDate, billResult, billDesc, yesVotes, noVotes, ""));
                            Log.i("thing", "Length of this line: " + temp.length + " " + billNum + " " + billSession + " " + billDate + " " + billResult + " " + billDesc + " " + yesVotes + " " + noVotes);
                        }
                    }


                    MyAdapter myAdapter = new MyAdapter(billList);
                    recyclerView.setAdapter(myAdapter);
                }

                catch (Exception e)
                {
                    Log.e("CSV Stream", e.getMessage());
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        mRequestQueue.add(request);
    }
}