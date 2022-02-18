package com.example.ourmp;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class JsonService{

    public MP parseFindMPAPI(String jsonMP){
        MP foundedMP = new MP();

        try{
            JSONObject jsonObject = new JSONObject(jsonMP);// root
            JSONArray MPArray = jsonObject.getJSONArray("objects");
            JSONObject MPObject = MPArray.getJSONObject(0);
            String photo_url = MPObject.getString("photo_url");
            String name = MPObject.getString("name");
            String district_name = MPObject.getString("district_name");
            String party = MPObject.getString("party_name");

            JSONObject labelObj = MPObject.getJSONObject("extra");
            String label = labelObj.getString("preferred_languages");

            foundedMP = new MP(name, party, label, district_name, photo_url);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return foundedMP;
    }

    public ArrayList<Activity> parseFindBills(String jsonBill){
        ArrayList<Activity> bills = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(jsonBill);// root
            JSONArray BillsArray = jsonObject.getJSONArray("objects");
            for(int i=0; i<BillsArray.length();i++) {
                JSONObject BillObject = BillsArray.getJSONObject(i);
                String billNumber = BillObject.getString("number");
                String billDesc = BillObject.getJSONObject("name").getString("en");
                String billDate = BillObject.getString("introduced");
                String billSession = BillObject.getString("session");
                bills.add(new Activity("", billNumber, billDesc, billDate, billSession));
            }
            bills.sort(Comparator.comparing(obj -> obj.activityDate));
            Collections.reverse(bills);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return bills;
    }
}
