package com.example.umyhnystma.matsvisitumea;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//import companydomain.visitumea.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Map extends SupportMapFragment implements OnMapReadyCallback {
    // Store instance variables

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;

    // Store instance variables based on arguments passed
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getMapAsync((MainActivity)getActivity());

        getMapAsync(this);

    }
    @Override
    public void onResume(){
        super.onResume();

    }

    // Inflate the view for the fragmentMap based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.activity_maps, container, false);

        Log.i("MIN_TAG", "MapFragment körs onCreateView");

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("MIN_TAG","onMapReady fragment körs");

        mMap = googleMap;

        LatLng umea = new LatLng(63.826499, 20.2742188);

        mMap.addMarker(new MarkerOptions().position(umea).title("Marker at Folkuniversitetet"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(umea));
    }
}
