package com.example.umyhnystma.matsvisitumea;


import android.content.Context;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.fitness.data.Application;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {



    protected MapView mMapView;
    protected GoogleMap googleMap;
    Boolean GPS, GeoLocationBasedGuide;


    double latitude, longitude;

    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    private ArrayList<Site> sitesArray = new ArrayList<>();

    MarkerOptions markerOptions;
    Marker marker;
    Marker backensKyrka;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);

        latitude = 63.8266178;
        longitude = 20.2740246;


        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Log.i("TAG", "I onInfoWindowClick mapFragment");

                //Visar LocationMessage

                /*

                int pos = mHashMap.get(marker);

                ((InfoDetailActivity)getActivity()).showLocationMessage(sitesArray.get(pos));

                */

            }
        });

        // latitude and longitude

        try{
            getMyLocation();
        }catch (Exception e){
            e.printStackTrace();
        }

        //setUpMapWithPosition(); Kanske ej nödvändigt. Flyttat till onResume

        try{
            int bundle = getArguments().getInt("KEY");

            if(bundle == 2){

                //setOutReligiosMarkers(); Gammalt


            }

        }catch (Exception e){
            Log.i("TAGGIE", "No bundle");
            e.printStackTrace();
        }


        return v;
    }



    @Override
    public void onResume(){
        super.onResume();

        setUpMapWithPosition();

        mMapView.onResume();
        Log.i("MIN_TAG","onResume i mapFragment");

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        Log.i("MIN_TAG","onPause i mapFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        Log.i("MIN_TAG","onDestroy i mapFragment");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
        Log.i("MIN_TAG","onLowMemory i mapFragment");
    }
    public void checkGPSState(){

        ((MainActivity)getActivity()).fragmentCheckGPS();

    }

    private void getMyLocation(){


        latitude = ((MainActivity)getActivity()).mCurrentLocation.getLatitude();
        longitude = ((MainActivity)getActivity()).mCurrentLocation.getLongitude();

    }

    public void setMyLocation(){

        setUpMapWithNewPosition();

    }

    public void setUpMapWithNewPosition() {

        marker.remove();

        markerOptions.position(new LatLng(latitude, longitude));

        // adding markerOptions
        marker = googleMap.addMarker(markerOptions);
        float zoom = googleMap.getCameraPosition().zoom;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(zoom).build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        // Perform any camera updates here

    }

    public void setUpMapWithPosition(){

        // create markerOptions
        markerOptions = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("You");

        // Changing markerOptions icon
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        // adding markerOptions
        marker = googleMap.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        // Perform any camera updates here

    }

    public void setOutReligiousMarkers(){

        //Sätter ut våra religösa platser på kartan

        backensKyrka = googleMap.addMarker(new MarkerOptions().position(new LatLng(63.8380731,20.1563725)).title("Backens kyrka"));

        Site backensKyrka = new Site();
        backensKyrka.setName("Backens kyrka");
        backensKyrka.setDescription("Description is a bit longer than title and therefore requires a bit more space");

        sitesArray.add(backensKyrka);

        for (int i = 0; i < sitesArray.size(); i++) {
            double latitude = sitesArray.get(i).getLatitude();
            double longitude = sitesArray.get(i).getLongitude();
            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(sitesArray.get(i).getName()));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mHashMap.put(marker, i);
        }

    }
    public Marker placeMarker(Site site) {

        //Metoden sköts från InfoDeatailActivity's onCreate. Den sätter ut Site-objekten på kartan.

        Marker m  = googleMap.addMarker(new MarkerOptions()

                .position(new LatLng(site.getLatitude(),site.getLongitude()))

                .title(site.getName()));

        return m;

    }

}

