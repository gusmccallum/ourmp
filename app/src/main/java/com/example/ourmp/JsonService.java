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
                String billURL = BillObject.getString("url");
                bills.add(new Activity(null, billNumber + " was introduced in session " + billSession, billDesc, billDate, billURL, null));
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


    public String parseMPDesc(String jsonString, String mpName){
        String desc = "" ;
        try{
            JSONObject jsonObject = new JSONObject(jsonString);// root
            JSONObject queryObject = jsonObject.getJSONObject("query");
            JSONArray searchArray = queryObject.getJSONArray("search");

            int index = 0;
            for(int i =0; i<searchArray.length(); i++){
                String title = searchArray.getJSONObject(i).getString("title");
                if(title.equals(mpName)){
                    index = i;
                    i = searchArray.length();
                }
            }
            desc = searchArray.getJSONObject(index).getString("snippet");
            desc = Html.fromHtml(desc).toString();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return desc;

    }

    public Bill parseMoreBillInfo(String jsonString){
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            String number = jsonObject.getString("number");
            String session = jsonObject.getString("session");
            String date = jsonObject.getString("introduced");
            //String result = jsonObject.getString("status_code");
            String result = jsonObject.getJSONObject("status").getString("en");
            String desc = jsonObject.getJSONObject("name").getString("en");

            JSONArray voteURLs = jsonObject.getJSONArray("vote_urls");
            String voteURL = "";
            if (voteURLs.length() > 0){
                voteURL = voteURLs.getString(0);
            }

            Bill bill = new Bill(number, session, date, result, desc, "0", "0", voteURL);
            return bill;
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<PartyVote> parseBillVote(String jsonString){
        ArrayList<PartyVote> allVotes = new ArrayList<>(0);

        try{
            JSONObject jsonObject = new JSONObject(jsonString);// root
            JSONArray VotesArray = jsonObject.getJSONArray("party_votes");

            for (int i = 0 ; i< VotesArray.length(); i++){
                JSONObject VoteObject = VotesArray.getJSONObject(i);
                String partyName = VoteObject.getJSONObject("party").getJSONObject("short_name").getString("en");
                String partyVote = VoteObject.getString("vote");
                PartyVote vote = new PartyVote(partyName, partyVote);
                allVotes.add(vote);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return allVotes;
    }
}