package com.example.ourmp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Subscribed2;
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

public class SettingsBill extends BaseActivity implements SettingsBillRecyclerInterface, DBManager.subObjCallback {

    RecyclerView recyclerView;
    private RequestQueue mRequestQueue;
    ArrayList<Bill> subscribedBills;
    SettingsBillRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_bill);
        subscribedBills = new ArrayList<Bill>();
        mRequestQueue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.SettingsBill_recycler);

        //check if user log in or not
        if (((MainApplication)getApplication()).getLogInStatus() == true) {
            DBManager dbManager = ((MainApplication)getApplication()).getDbManager();
            dbManager.getSubscriptionObject();
            dbManager.setSubObjCallbackInstance(this);


            //initialize views
            recyclerAdapter = new SettingsBillRecyclerAdapter(subscribedBills, this);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        }else{
            Toast.makeText(this, "Sign in to edit your subscriptions.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(int position) {
        subscribedBills.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }

    @Override
    public void getSub(Subscribed2 cbReturnSub) {
        List<String> returnedSubbedBills = cbReturnSub.getSubscribedBills();
        for (int i = 0; i < returnedSubbedBills.size(); i++) {
            fetchBillData(returnedSubbedBills.get(i));
        }

    }

    private void fetchBillData(String billNum)
    {
        String url = "https://www.parl.ca/legisinfo/en/bill/44-1/" + billNum + "/json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONObject BillObject = response.getJSONObject("0");
                    String title = billNum;
                    String description = BillObject.getString("LongTitleEn");
                    String status = BillObject.getString("StatusNameEn");
                    String date = BillObject.getString("LatestBillEventDateTime");
                    String billSponsorName = BillObject.getString("SponsorPersonOfficialFirstName") + "%20" + BillObject.getString("SponsorPersonOfficialLastName");
                    JSONArray votesArray = BillObject.getJSONArray("HouseVoteDetails");
                    JSONObject votesObject = votesArray.getJSONObject(votesArray.length());
                    String yeas = votesObject.getString("DivisionVotesYeas");
                    String nays = votesObject.getString("DivisionVotesNays");

                    Bill newBill = new Bill(billNum, "44-1", date, status, description, yeas, nays, "");
                    subscribedBills.add(newBill);
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
