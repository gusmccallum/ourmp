package com.example.ourmp;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class CompareMPActivity extends BaseActivity
        implements NetworkingService.NetworkingListener, CompareSearchAdapter.OnItemClickListener{

    NetworkingService networkingService;
    JsonService jsonService;
    ArrayList<Ballot> allBallotFromMP1 = new ArrayList<>(0);

    MP mpObj1, mpObj2;
    ImageView mp1_img, mp2_img;
    TextView mp1_name, mp2_name;
    MP1CompareAdapter adapter1, adapter2;
    CompareSearchAdapter searchAdapter;
    RecyclerView recyclerView1, recyclerView2;
    //SearchView searchView;
    String str_url = "https://represent.opennorth.ca/representatives/house-of-commons/?limit=400";
    ArrayList<MP> MPArrayList = new ArrayList<>();
    RecyclerView recyclerView_event;
    EditText SearchText;
    NestedScrollView nestedView;
    ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);
    ArrayList<Ballot> tempbollotArray;
    ProgressDialog progressDialog;
    RelativeLayout mp2_relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_compare_mp);

        Bundle bundleFromMainActivity = getIntent().getBundleExtra("bundle");
        allBallotFromMP1 = bundleFromMainActivity.getParcelableArrayList("ballotList");
        mpObj1 = getIntent().getParcelableExtra("MPObj");

        mp1_img = findViewById(R.id.mp1_img);
        mp2_img = findViewById(R.id.mp2_img);
        mp1_name = findViewById(R.id.mp1_name_txt);
        mp2_name = findViewById(R.id.mp2_name_txt);
        //searchView = findViewById(R.id.compare_search);
        nestedView = findViewById(R.id.nested_scroll_view);

        recyclerView1 = findViewById(R.id.mp1_list);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2 = findViewById(R.id.mp2_list);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mp2_relative = findViewById(R.id.mp2_list_relativeLayout);

        networkingService = ( (MainApplication)getApplication()).getNetworkingService();
        jsonService = ( (MainApplication)getApplication()).getJsonService();
        networkingService.listener = this;

        networkingService.getImageData(mpObj1.getPhotoURL());
        //show MP1's info
        mp1_name.setText(mpObj1.getName());
        adapter1 = new MP1CompareAdapter(this, allBallotFromMP1, 1);//enter 1 for MP1
        recyclerView1.setAdapter(adapter1);


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
                nestedView.setVisibility(View.VISIBLE);
                mp2_relative.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterQuery(editable.toString());
            }
        });

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
        MP tempMp = jsonService.parseMoreInfoAPI(jsonString);
        mpObj2.setBallotURL(tempMp.getBallotURL());
        networkingService.fetchBallot(mpObj2.getBallotURL());
    }

    @Override
    public void APIBallotListener(String jsonString) {
        tempbollotArray = new ArrayList<>(0);

        allBallotFromMP = jsonService.parseBallots(jsonString);
        for(int i=0; i<allBallotFromMP.size(); i++){
            networkingService.fetchVote(allBallotFromMP.get(i).getVoteURL());
        }
    }

    @Override
    public void APIVoteListener(String jsonString) {


        tempbollotArray.add(jsonService.parseVote(jsonString));
        //list date and bill desc, same size with allBollotFromMP
        if(tempbollotArray.size() == allBallotFromMP.size()){
            //copy all date and bill number
            for(int i=0; i<allBallotFromMP.size(); i++){

                if(!tempbollotArray.get(i).getBillNum().equals("null")){
                    allBallotFromMP.get(i).setBillNum(tempbollotArray.get(i).getBillNum());
                    allBallotFromMP.get(i).setDate(tempbollotArray.get(i).getDate());

                }

            }

            ArrayList<Ballot> allBallotFromMP2 = new ArrayList<>(0);
            //choose ballots only that has valid bill number
            for(int j=0; j<allBallotFromMP.size(); j++){
                if(allBallotFromMP.get(j).getBillNum() != null){
                    allBallotFromMP2.add(allBallotFromMP.get(j));
                }
            }

            adapter2 = new MP1CompareAdapter(this, allBallotFromMP2, 2);//enter 1 for MP1
            recyclerView2.setAdapter(adapter2);
            mp2_relative.setVisibility(View.VISIBLE);


            progressDialog.dismiss();
        }

    }

    @Override
    public void APIMPDescListener(String jsonString) {

    }

    @Override
    public void APIBillsListener(String jsonString) {

    }

    @Override
    public void onItemClick(MP mpObj) {
        mpObj2 = mpObj;
        nestedView.setVisibility(View.GONE);
        mp2_img.setImageBitmap(mpObj2.getPhoto());
        mp2_name.setText(mpObj2.getName());
        mpObj2.setParty(mpObj.getParty());

        progressDialog = new ProgressDialog(CompareMPActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading vote list...");
        progressDialog.show();

        networkingService.fetchMoreMPInfo(mpObj2.getName());
    }

    private void getMPsList() {

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
                        member.setParty(jsonObject1.getString("party_name"));
                        member.setPhotoURL(jsonObject1.getString("photo_url"));
                        new Search.DownloadImage(member).execute(member.getPhotoURL());

                         MPArrayList.add(member);
                    }

                    searchAdapter = (new CompareSearchAdapter(MPArrayList, CompareMPActivity.this));
                    recyclerView_event.setAdapter(searchAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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
                    searchAdapter.update();
                }
            }
            this.searchAdapter.setFilter(filterdNames);
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
