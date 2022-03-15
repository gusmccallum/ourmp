package com.example.ourmp;


import android.graphics.Bitmap;

import android.text.Html;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

            foundedMP = new MP(name, party, label, district_name, photo_url,
                    null, null, null, null);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return foundedMP;
    }

    public ArrayList<Activity> parseFindBills(String jsonBill) {
        ArrayList<Activity> bills = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonBill);// root
            JSONArray BillsArray = jsonObject.getJSONArray("objects");
            for (int i = 0; i < BillsArray.length(); i++) {
                JSONObject BillObject = BillsArray.getJSONObject(i);
                String billNumber = BillObject.getString("number");
                String billDesc = BillObject.getJSONObject("name").getString("en");
                String billDate = BillObject.getString("introduced");
                String billSession = BillObject.getString("session");
                bills.add(new Activity(null, billNumber + " was introduced in session " + billSession, billDesc, billDate));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bills;
    }

    public MP parseMoreInfoAPI(String jsonMP){
        //return with twitter, phone, email
        MP foundedMP = new MP();
        JSONArray twitterPart = null;

        try{
            JSONObject jsonObject = new JSONObject(jsonMP);// root
            JSONObject twitterObject = jsonObject.getJSONObject("other_info");
            twitterPart = twitterObject.getJSONArray("twitter");
// noticed that some of MP api does not have twitter part!
// try to fetch twitter part and store it in the temp json array
        }catch (JSONException ignored) {
        }

        //if it was null, fetch others except twitter
        if(twitterPart == null){
            try{
                JSONObject jsonObject = new JSONObject(jsonMP);// root
                String phone = jsonObject.getString("voice");
                String email = jsonObject.getString("email");

                JSONObject relatedObject = jsonObject.getJSONObject("related");
                String ballotURL = relatedObject.getString("ballots_url");

                foundedMP = new MP(null, null, null, null, null,
                        "", phone, email, ballotURL);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            try{
                JSONObject jsonObject = new JSONObject(jsonMP);// root
                JSONObject twitterObject = jsonObject.getJSONObject("other_info");
                JSONArray twitterName = twitterObject.getJSONArray("twitter");
                String twitter = twitterName.getString(0);


                String phone = jsonObject.getString("voice");
                String email = jsonObject.getString("email");

                JSONObject relatedObject = jsonObject.getJSONObject("related");
                String ballotURL = relatedObject.getString("ballots_url");

                foundedMP = new MP(null, null, null, null, null,
                        twitter, phone, email, ballotURL);
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return foundedMP;
    }

    public ArrayList<Ballot> parseBallots(String jsonBallot){
        //return the activities about ballot
        ArrayList<Ballot> allBallotFromMP = new ArrayList<>(0);

        try{
            JSONObject jsonObject = new JSONObject(jsonBallot);// root
            JSONArray BallotArray = jsonObject.getJSONArray("objects");

            for (int i = 0 ; i< BallotArray.length(); i++){
                JSONObject BallotObject = BallotArray.getJSONObject(i);

                String ballot = BallotObject.getString("ballot");
                String mpURL = BallotObject.getString("politician_url");
                String voteUrl = BallotObject.getString("vote_url");

                if(!ballot.equals("Yes") && !ballot.equals("No")){
                    ballot = "";
                }
                Ballot newBallot = new Ballot(ballot, mpURL, voteUrl, null, null);
                allBallotFromMP.add(newBallot);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return allBallotFromMP;
    }

    public Ballot parseVote(String jsonMP){
        //return bill numbers from url of ballot!!
        Ballot ballot = new Ballot();

        try{
            JSONObject jsonObject = new JSONObject(jsonMP);// root
            JSONObject descObject = jsonObject.getJSONObject("description");
            String str = descObject.getString("en");
            String date = jsonObject.getString("date");

           /* int index = str.indexOf("Bill C-");
            if (index > 0)
            {
                str = str.substring(index, index+9);
            }*/
            ballot.setDate(date);
            ballot.setBillNum(str);


        }catch (JSONException e) {
            e.printStackTrace();
        }
        return ballot;
    }
    public String parseMPDesc(String jsonString){
        String desc = "" ;
        try{
            JSONObject jsonObject = new JSONObject(jsonString);// root
            JSONObject queryObject = jsonObject.getJSONObject("query");
            JSONArray searchArray = queryObject.getJSONArray("search");

            desc = searchArray.getJSONObject(0).getString("snippet");
            desc = Html.fromHtml(desc).toString();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return desc;

    }
}
