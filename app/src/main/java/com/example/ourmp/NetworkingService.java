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
    String listOfBills = "https://api.openparliament.ca/bills/?introduced__gt=2018-01-01&format=json&limit=200";
    String listOfMPs = "https://represent.opennorth.ca/representatives/house-of-commons/?limit=50";


    String MpPageURL1 = "https://api.openparliament.ca/politicians/";
    String formatJson = "/?format=json";

    String openMPURL = "https://api.openparliament.ca/";

    String MPdescURL = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=";
    String formatJson2 = "&format=json";
    int status;
    public static final ExecutorService networkingExecutor = Executors.newFixedThreadPool(8);
    static Handler networkHander = new Handler(Looper.getMainLooper());

    interface NetworkingListener{
        void APINetworkListner(String jsonString); //status = 0
        void APINetworkingListerForImage(Bitmap image);//status = 0
        void APIMPMoreInfoListener(String jsonString); // status = 4
        void APIBallotListener(String jsonString);//status = 1
        void APIVoteListener(String jsonString); //status = 2
        void APIMPDescListener(String jsonString); // status = 3
        void APIBillsListener(String jsonString); //status = 5
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
        if(fullName.contains(" ")) {
            String[] splitStr = fullName.toLowerCase()
                    .trim().split("\\s+");
            completeURL = MpPageURL1 + splitStr[0] + "-" + splitStr[1] + formatJson;

        }else{
            completeURL = MpPageURL1 + fullName + formatJson;
        }
        connect(completeURL);
    }

    public void fetchBallot(String ballotUrl){
        status = 1;
        String completeURL = openMPURL+ballotUrl;
        connect(completeURL);
    }

    public void fetchVote(String voteURL){
        status = 2;
        String completeURL = openMPURL+voteURL;
        connect(completeURL);
    }
    public void fetchMPDesc(String fullName){
        status = 3;
        String completeURL;
        if(fullName.contains(" ")) {
            String[] splitStr = fullName.trim().split("\\s+");
            completeURL = MPdescURL + splitStr[0] + "%20" + splitStr[1] + formatJson2;

        }else{
            completeURL = MPdescURL + fullName + formatJson2;
        }
        connect(completeURL);

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
                        int read = 0;
                        while ((read = inputStreamReader.read()) != -1) {// json integers ASCII
                            char c = (char) read;
                            jsonString += c;
                        }
                        // dataTask in ios
                        final String finalJson = jsonString;
                        networkHander.post(new Runnable() {
                            @Override
                            public void run() {
                                //send data to main thread
                                if(status == 0)
                                {
                                    listener.APINetworkListner(finalJson);
                                }
                                else if(status == 1)
                                {
                                    listener.APIBallotListener(finalJson);
                                }
                                else if(status == 2){
                                    listener.APIVoteListener(finalJson);
                                }
                                else if(status == 3){
                                    listener.APIMPDescListener(finalJson);
                                }
                                else if(status == 4){
                                    listener.APIMPMoreInfoListener(finalJson);
                                }else if(status == 5){
                                    listener.APIBillsListener(finalJson);
                                }
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
}
