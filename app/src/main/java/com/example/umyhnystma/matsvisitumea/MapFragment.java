package com.example.umyhnystma.matsvisitumea;


import android.content.Context;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    ToggleChange myToggleChange;
    MapView mMapView;
    private GoogleMap googleMap;
    Boolean GPS, GeoLocationBasedGuide;


    double latitude, longitude;

    MarkerOptions markerOptions;
    Marker marker;

    @Override
    public void onAttach(Context context){
        //Interface mellan MapFragment och ToggleButtons via MainActivity
        super.onAttach(context);
        myToggleChange = (ToggleChange)getActivity();
    }

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

//        googleMap.getMyLocation(); KRASCH

        // latitude and longitude

        try{
            getMyLocation();
        }catch (Exception e){
            e.printStackTrace();
        }

        setUpMapWithPosition();


        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
        Log.i("MIN_TAG","onResume");

    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        Log.i("MIN_TAG","onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        Log.i("MIN_TAG","onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
        Log.i("MIN_TAG","onLowMemory");
    }
    public void checkGPSState(){

        if(GPS){
            //GPS should be true

            Log.i("MIN_TAG", "GPS is true, see GPS = " + GPS);

        }else{
            //GPS should be false

            Log.i("MIN_TAG", "GPS is false, see GPS = " + GPS);

        }

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
                new LatLng(latitude, longitude)).title("Hello Maps");

        // Changing markerOptions icon
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding markerOptions
        marker = googleMap.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        // Perform any camera updates here

    }

}

