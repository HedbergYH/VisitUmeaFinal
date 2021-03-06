package com.example.umyhnystma.matsvisitumea;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */

// Fragmantet ska innehålla en karta med egen position och med position av vald byggnad
public class MapTrackFragment extends Fragment {


    private static final String HISTORICAL = "Historical";

    FragmentManager fm;
    FragmentTransaction trans;
    Fragment fragment;
    SupportMapFragment map;

    boolean showButtons;


    MapFragment mapFragment;
    Site mySelectedSite;
    private HashMap <Marker, Site> siteMarkerMap;

    View view;
    RelativeLayout mapContainer;
    LinearLayout  firstLinear;

    public MapTrackFragment() {
        // Required empty public constructor
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        siteMarkerMap = new HashMap<Marker, Site>();

        Bundle bundle2 = getArguments();
        showButtons = bundle2.getBoolean("KEY");
        mySelectedSite = (Site)bundle2.getSerializable("KEY_SERIALIZABLE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map_track, container, false);
        mapContainer = (RelativeLayout) view.findViewById(R.id.mapContainer);
        firstLinear = (LinearLayout) view.findViewById(R.id.firstLinear);

        trans = getChildFragmentManager().beginTransaction(); // Måste vara getChildFragmentManager() då det är ett fragment i ett fragment

      //  Bundle bundle = new Bundle();
      //  bundle.putBoolean("KEY",true);

        mapFragment = new MapFragment();
      //  mapFragment.setArguments(bundle);
        trans.add(R.id.firstLinear, mapFragment);
        trans.commit();
        return view;
    }



    public void onStart(){
        super.onStart();

        setUpSelectedSite();

    }

    public void setUpSelectedSite(){
            Marker m = mapFragment.placeMarker(mySelectedSite);
            siteMarkerMap.put(m, mySelectedSite);
    }

    public void onResume(){
        super.onResume();


    }
}
