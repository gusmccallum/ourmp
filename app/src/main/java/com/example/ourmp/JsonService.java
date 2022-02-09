package com.example.ourmp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class JsonService {
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
}
