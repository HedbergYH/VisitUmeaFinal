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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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
public class MapFragment extends Fragment implements OnMapReadyCallback{  // onMapReadyCallback för att metoden onMapReady ska finnas



    protected MapView mMapView;

    protected GoogleMap googleMap;
    Boolean GPS, GeoLocationBasedGuide;


    double latitude, longitude;

    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    private ArrayList<Site> sitesArray = new ArrayList<>();
    private View v;
    private boolean showNavigationButtons;

    MarkerOptions markerOptions;
    Marker marker;
    Marker backensKyrka;

    Bundle bundle;

    UiSettings uiSettings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
         v = inflater.inflate(R.layout.fragment_map, container,
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

        bundle = getArguments();
//        showNavigationButtons = bundle.getBoolean("KEY");

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


    // onViewCreated anropas efter att onCreateView har körts
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Log.i("MIN_TAG","i onViewCreated i mapFragment");
        mMapView.onResume();
        mMapView.getMapAsync(this);     //when you already implement OnMapReadyCallback in your fragment
    }

    // Nedan: onMapReady anropas när kartan är klar, onMapReady implementeras med interfacet OnMapReadyCallback
    // zoom-knapp placeras ut
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("MIN_TAG","i onMapReady i mapFragment");
         uiSettings = googleMap.getUiSettings();            // uiSettings hämtas från googleMap-objektet
         uiSettings.setZoomControlsEnabled(true);           // zoom-knapp sätts på uiSettings-objektet
        //Disable Map Toolbar:
        Log.i("MIN_TAG","i onMapReady i mapFragment, showNavigationButtons är:"+ showNavigationButtons);
   //     if (showNavigationButtons)
         //   uiSettings.setMapToolbarEnabled(true);
       // else
        uiSettings.setMapToolbarEnabled(false);
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

    public void setMyLocationMainMap(){
        setUpMapWithNewPositionCameraFollow();
    }

    public void setMyLocationSitesMap(){
        setUpMapWithNewPositionCameraUnfollow();
    }

    public void setUpMapWithNewPositionCameraFollow() {

        marker.remove();

        markerOptions.position(new LatLng(latitude, longitude));

        // adding markerOptions
        marker = googleMap.addMarker(markerOptions);
        float zoom = googleMap.getCameraPosition().zoom;
        if (zoom < 8){
            zoom = 8;
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(zoom).build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        // Perform any camera updates here
    }

    public void setUpMapWithNewPositionCameraUnfollow() {

        marker.remove();
        markerOptions.position(new LatLng(latitude, longitude));
        marker = googleMap.addMarker(markerOptions);


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

    public Marker placeMarker(Site site) {

        //Metoden sköts från InfoDeatailActivity's onCreate. Den sätter ut Site-objekten på kartan.
        if (googleMap==null)
            Log.i("MIN_TAG", " I MapFragment, placeMarker, googleMap==null");
        Marker m  = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(site.getLatitude(),site.getLongitude()))
                .title(site.getName()));
        return m;
    }
}

