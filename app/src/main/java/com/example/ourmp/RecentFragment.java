package com.example.ourmp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public  class RecentFragment extends Fragment
{
    List<Bill> billList;
    RecyclerView recyclerView;
    private RequestQueue mRequestQueue;

    public RecentFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        billList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        initData();
        initRecyclerView();
        return view;
    }

    private void initRecyclerView()
    {
        MyAdapter myAdapter = new MyAdapter(billList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initData()
    {
        String url = "https://api.openparliament.ca/votes/?session=43-2&format=json";

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