package com.example.ourmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
        String url = "https://api.openparliament.ca/votes/?session=44-1&format=json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONArray BillsArray = response.getJSONArray("objects");

                    for (int i = 0; i < BillsArray.length(); i++)
                    {
                        JSONObject BillObject = BillsArray.getJSONObject(i);

                        if(BillObject.getString("bill_url") != ("null"))
                        {
                            String billNum = BillObject.getString("bill_url");
                            String[] temp = billNum.split("/");
                            billNum = temp[3];
                            String billSession = BillObject.getString("session");
                            String billDate = BillObject.getString("date");
                            String billResult = BillObject.getString("result");
                            String billDesc = BillObject.getJSONObject("description").getString("en");
                            String yesVotes = BillObject.getString("yea_total");
                            String noVotes = BillObject.getString("nay_total");
                            billList.add(new Bill(billNum, billSession, billDate, billResult, billDesc, yesVotes, noVotes, ""));
                        }
                    }

                    MyAdapter myAdapter = new MyAdapter(billList);
                    recyclerView.setAdapter(myAdapter);
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }
}