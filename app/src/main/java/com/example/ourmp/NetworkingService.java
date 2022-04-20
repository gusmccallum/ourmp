package com.example.ourmp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkingService {
    String findMPURL = "https://represent.opennorth.ca/representatives/house-of-commons/?point=";

    String listOfBills = "https://api.openparliament.ca/bills/?session=44-1&format=json&limit=323";

    String MpPageURL1 = "https://api.openparliament.ca/politicians/";
    String formatJson = "/?format=json";

    //String openMPURL = "https://api.openparliament.ca/";

    String MPdescURL = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=";
    String formatJson2 = "&format=json";


    int status;
    public static final ExecutorService networkingExecutor = Executors.newFixedThreadPool(8);
    static Handler networkHander = new Handler(Looper.getMainLooper());

    interface NetworkingListener{
        void APINetworkListener(String jsonString); //status = 0
        void APINetworkingListerForImage(Bitmap image);//status = 0
        void APIMPMoreInfoListener(String jsonString); // status = 4
        void APIMPDescListener(String jsonString); // status = 3
        void APIBillsListener(String jsonString); //status = 5
        void APIMoreBillInfoListener(String jsonString); //status = 6
        void APIParseBillVote(String jsonString);
        void APINetworkingListerForImage2(Bitmap image);
    }

    NetworkingListener listener;

    public void fetchMPData(Double lat, Double lng){
        status = 0;
        String completeURL = findMPURL + lat + "," + lng;
        connect(completeURL);
    }

    public void fetchBillsData() {
        status = 5;
        connect(listOfBills);
    }

    public void fetchMoreMPInfo(String fullName){
        status = 4;
        String completeURL;
        String formattedName = formatName(fullName, "-");

        completeURL = MpPageURL1 + formattedName.toLowerCase() + formatJson;
        connect(completeURL);
    }
    public void fetchMPDesc(String fullName){
        status = 3;
        String completeURL;
        String formattedName = formatName(fullName, "%20");
        completeURL = MPdescURL + formattedName + formatJson2;
        //connect(completeURL);
        connect2(completeURL);
    }
    public void fetchMoreBillInfo(String url){
        status = 6;
        connect(url);
    }

    public void fetchBillVotes(String url){
        status = 7;
        connect(url);
    }

    private void connect(String completeURL) {
        networkingExecutor.execute(new Runnable() {
            String jsonString = "";
            @Override
            public void run() {

                HttpURLConnection httpURLConnection = null;
                try {
                    URL urlObject = new URL(completeURL);
                    httpURLConnection = (HttpURLConnection) urlObject.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type","application/json");
                    int statues = httpURLConnection.getResponseCode();

                    if ((statues >= 200) && (statues <= 299)) {
                        InputStream in = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(in);
                        int read;
                        while ((read = inputStreamReader.read()) != -1) {// json integers ASCII
                            char c = (char) read;
                            jsonString += c;
                        }
                        // dataTask in ios
                        final String finalJson = jsonString;
                        networkHander.post(() -> {
                            //send data to main thread
                            if(status == 0)
                            {
                                listener.APINetworkListener(finalJson);
                            }
                            else if(status == 4){
                                listener.APIMPMoreInfoListener(finalJson);
                            }else if(status == 5){
                                listener.APIBillsListener(finalJson);
                            }else if(status == 6){
                                listener.APIMoreBillInfoListener(finalJson);
                            }else if(status == 7){
                                listener.APIParseBillVote(finalJson);
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    httpURLConnection.disconnect();
                }
            }
        });
    }

    private void connect2(String completeURL) {
        networkingExecutor.execute(new Runnable() {
            String jsonString = "";
            @Override
            public void run() {

                HttpURLConnection httpURLConnection = null;
                try {
                    URL urlObject = new URL(completeURL);
                    httpURLConnection = (HttpURLConnection) urlObject.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type","application/json");
                    int statues = httpURLConnection.getResponseCode();

                    if ((statues >= 200) && (statues <= 299)) {
                        InputStream in = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(in);
                        int read;
                        while ((read = inputStreamReader.read()) != -1) {// json integers ASCII
                            char c = (char) read;
                            jsonString += c;
                        }
                        // dataTask in ios
                        final String finalJson = jsonString;
                        networkHander.post(() -> {
                            //send data to main thread
                           listener.APIMPDescListener(finalJson);
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    httpURLConnection.disconnect();
                }
            }
        });
    }

    public void getImageData(String imgURL){
        String completeURL = imgURL;
        networkingExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL urlObj = new URL(completeURL);
                    InputStream in = ((InputStream)urlObj.getContent());
                    Bitmap imageData = BitmapFactory.decodeStream(in);
                    networkHander.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.APINetworkingListerForImage(imageData);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getImageData2(String imgURL){
        String completeURL2 = imgURL;
        networkingExecutor.execute(() -> {
            try {
                URL urlObj = new URL(completeURL2);
                InputStream in = ((InputStream)urlObj.getContent());
                Bitmap imageData = BitmapFactory.decodeStream(in);
                networkHander.post(() -> listener.APINetworkingListerForImage2(imageData));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public String formatName(String fullName, String replacement){

        String formattedStr;

        if(fullName.equals("Robert J. Morrissey")){
            formattedStr = "Bobby Morrissey";
        }
        else if(fullName.equals("Candice Bergen")){
            formattedStr = "Candice Hoeppner";
        }
        else{

            formattedStr = fullName;
            if(fullName.equals("Harjit S. Sajjan")){
                formattedStr = "Harjit S Sajjan";
            }
            //change all non-enlgish letter to english
            formattedStr = formattedStr.replace("\u00e9", "e")
                    .replace("\u00e8", "e")
                    .replace("\u00e7", "c")
                    .replace("\u00c9", "e")
                    .replace("\u00eb", "e");
            //remove all '
            formattedStr = formattedStr.replace("'", "");
            //remove middle name with dot(.)
            int dot = formattedStr.indexOf(".");
            if(dot > -1){
                //ex - Michael V. McLeod, dot=9
                String s2 = formattedStr.substring(dot+1); //" McLeod"
                String s1 = formattedStr.substring(0, dot-2); // "Michael"
                formattedStr = s1+s2; //"Michael McLeod"
            }
        }

        //replace white space with replacement - or %20
        if(formattedStr.contains(" ")) {
            String[] splitStr = formattedStr.trim().split("\\s+");
            //ex) Adam, van, Koeverden
            String str="";
            for(int i=0; i<splitStr.length; i++){ //3
                if(i == splitStr.length-1){
                    str += splitStr[i]; //str = Adam-van-Koeverdeny
                }
                else{
                    str += splitStr[i]+replacement; //str = Adam-van-
                }
            }
            formattedStr = str;
        }

        return formattedStr;
    }
}
