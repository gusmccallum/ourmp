package com.example.ourmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class Search extends AppCompatActivity  {

    ArrayList<MP> MPArrayList = new ArrayList<>();
    RecyclerView recyclerView_event;
    EditText SearchText;
    private SearchAdapter adapter;
    String str_url = "https://represent.opennorth.ca/representatives/house-of-commons/?limit=400";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViewById(R.id.imgbck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        MPArrayList = new ArrayList<>();
        recyclerView_event = findViewById(R.id.recyclerView_data);
        recyclerView_event.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        getMPsList();
        this.SearchText = (EditText) findViewById(R.id.search_input);
        this.SearchText.addTextChangedListener(new TextWatcher() {

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

    private void getMPsList() {
        final ProgressDialog progressDialog = new ProgressDialog(Search.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, str_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    MPArrayList = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayMps = jsonObject.getJSONArray("objects");

                    for (int i = 0; i < jsonArrayMps.length(); i++) {
                        MP member = new MP();
                        JSONObject jsonObject1 = jsonArrayMps.getJSONObject(i);
                        member.setName(jsonObject1.getString("name"));
                        member.setRiding(jsonObject1.getString("district_name"));
                        member.setParty(jsonObject1.getString("party_name"));
                        member.setPhotoURL(jsonObject1.getString("photo_url"));
                        new DownloadImage(member).execute(member.getPhotoURL());

                        MPArrayList.add(member);
                    }

                    adapter = (new SearchAdapter(MPArrayList, Search.this));
                    recyclerView_event.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

    public void filterQuery(String text) {
        if (!text.isEmpty()) {
            recyclerView_event.setVisibility(View.VISIBLE);
            ArrayList<MP> filterdNames = new ArrayList<>();
            for (MP mp : this.MPArrayList) {
                if (mp.getName().toLowerCase().contains(text.toLowerCase())) {
                    filterdNames.add(mp);
                    adapter.update();
                }
            }
            this.adapter.setFilter(filterdNames);
        } else {
            recyclerView_event.setVisibility(View.GONE);
        }

    }


    public static class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        MP member;

        public DownloadImage(MP member) {
            this.member = member;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            member.setPhoto(result);
        }
    }

}