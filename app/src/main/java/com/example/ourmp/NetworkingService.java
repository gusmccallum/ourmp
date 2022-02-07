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

    public static final ExecutorService networkingExecutor = Executors.newFixedThreadPool(4);
    static Handler networkHander = new Handler(Looper.getMainLooper());

    interface NetworkingListener{
        void APINetworkListner(String jsonString);
        void APINetworkingListerForImage(Bitmap image);
    }

    NetworkingListener listener;

    public void fetchMPData(Double lat, Double lng){
        String completeURL = findMPURL + lat + "," + lng;
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
                                listener.APINetworkListner(finalJson);
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
