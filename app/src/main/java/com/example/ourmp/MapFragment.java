package com.example.ourmp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Getting current location and request location permission
public class MapFragment extends Fragment implements OnMapReadyCallback{

    FusedLocationProviderClient fusedLocationProviderClient; //to get current location
    LatLng latLng; // lat and long object
    SupportMapFragment supportMapFragment; //Map fragment to show a map
    MarkerOptions markerOptions = new MarkerOptions();
    SearchView searchView;
    public static final ExecutorService networkingExecutorForSearchLocation = Executors.newFixedThreadPool(2);
    //another thread is needed to use any kind of networking service: geocoder here
    Geocoder geocoder; //for search location and get the location info
    Address address = null; // to use geocoder
    LocationListener mCallback; // interface to pass info to location activity


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //initialize
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        searchView = view.findViewById(R.id.location_search);
        geocoder = new Geocoder(getActivity());

        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        //request for location access (two)
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        //search function
        executeSearch();

        return view;
    }

    private void executeSearch() {
        //use different thread for searching function
        networkingExecutorForSearchLocation.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        //when text was submitted on the search bar
                        public boolean onQueryTextSubmit(String query) {
                            String zip = searchView.getQuery().toString();
                            //address list to store the location information
                            List<Address> addressList = null;
                            //if the text is not null or not empty
                            if(zip!=null || !zip.equals("")){

                                try {
                                    //get the location information by search text
                                    addressList = geocoder.getFromLocationName(zip, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                               if(addressList != null && addressList.size() > 0)
                                {
                                    //store the first result in Address object
                                    address = addressList.get(0);
                                    //set latlng using lat, lng from address object
                                    //latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                    latLng = new LatLng(43.707343,-79.394242);
                                    if(address.getCountryName().equals("Canada")){
                                        //show the map only the result is in Canada
                                        supportMapFragment.getMapAsync(MapFragment.this);
                                    }else{
                                        //if it's not Canada, popup message to enter address inside of Canda
                                        Toast.makeText(getActivity(), "Please enter address in Canada", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            return false;
                        }
                        @Override
                        public boolean onQueryTextChange(String newText) {
                            return false;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION,false);
                        //above is to set up for location access
                        if (fineLocationGranted != null && fineLocationGranted
                        && coarseLocationGranted != null && coarseLocationGranted) {
                            // location access granted, show popup message
                            Toast.makeText(getActivity(), "Location permission Granted", Toast.LENGTH_SHORT).show();
                            //get current location
                            getCurrentLocation();
                        } else {
                            //permission denied, popup message
                            Toast.makeText(getActivity(), "Location permission Denied", Toast.LENGTH_SHORT).show();
                            //set the default location as Ottawa
                            latLng = new LatLng(45.388929, -75.701603);
                            //show in map
                            supportMapFragment.getMapAsync(this);

                        }
                    }
            );

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //get the current location
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                List<Address> addressList2 = null;
                if(location != null){
                    try{
                        //creating address list using geocoder from current location's lat, long
                        addressList2 = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    latLng = new LatLng(43.707343,-79.394242);

                    if(addressList2 != null && addressList2.size() > 0){
                        //set Address object as current location info
                        address = addressList2.get(0);

                        //if the address obj is Canada
                        if(address.getCountryName().equals("Canada")){
                            //show in a map
                            supportMapFragment.getMapAsync(MapFragment.this);
                        }
                        else{
                            //if not in Canada, show popup message
                            Toast.makeText(getActivity(), "Please use search option as you are outside of Canada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    //Show the location on a map
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //clear google map in case it has a marker
        googleMap.clear();
        //add circle around the address on a map
        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(500)
                .strokeColor(Color.RED)
                .fillColor(0x30ff0000));
        //add red marker on the location
        googleMap.addMarker(markerOptions.position(latLng));
        //move camera to the location
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

        //after showing location on a map, pass the postal code to location activity
        mCallback.MPCardLocation(address.getLatitude(), address.getLongitude());
    }

    //To implement interface?
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (LocationListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }
}