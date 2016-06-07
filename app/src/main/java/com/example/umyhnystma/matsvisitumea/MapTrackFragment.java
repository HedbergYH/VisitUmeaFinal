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
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */

// Fragmantet ska innehålla en karta med egen position och med posittion av vald byggnad
public class MapTrackFragment extends Fragment {


    private static final String HISTORICAL = "Historical";

    FragmentManager fm;
    FragmentTransaction trans;
    Fragment fragment;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    Location mCurrentLocation;

     MapFragment mapFragment;

    private HashMap <Marker, Site> siteMarkerMap;

    View view;
    TextView textView1_InMapTrackFragment;
    Button backButton,showMapButton;
    RelativeLayout mapContainer;
    LinearLayout  firstLinear;

    //   OnClick myClicker;

    public MapTrackFragment() {
        // Required empty public constructor
    }


    //   @Override
    //   public void onAttach(Context context) {  // fragmentets har skapats
        //       super.onAttach(context);
    //    myClicker = (OnClick) context;       // myclicker skapas för att
        //   }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_info_detail);

        siteMarkerMap = new HashMap<Marker, Site>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map_track, container, false);
        mapContainer = (RelativeLayout) view.findViewById(R.id.mapContainer);
        firstLinear = (LinearLayout) view.findViewById(R.id.firstLinear);
        backButton = (Button)view.findViewById(R.id.backButton);

        // Kartfragmentet(MapFragment)  blåses upp i  "firstLinear" vilken är en container
        // som ligger i detta fragment(MapTrackFragment)
        fm = getActivity().getSupportFragmentManager();
        trans = fm.beginTransaction();
        mapFragment = new MapFragment();
        trans.add(R.id.firstLinear, mapFragment);
        trans.commit();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();

                if (count == 0) {
                    getActivity().finish();
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });
        return view;
    }

    public void onStart(){
        super.onStart();
       // setUpSelectedSite();

    }

    public void setUpSelectedSite(){
        Site sävargården = new Site("Sävargården", "Fint hus det här", 63.828462,20.290932, HISTORICAL,"http://www.umea.se/images/18.73474df7141ec1b19d14e9f/1383576061560/S%C3%A4varg%C3%A5rden_h.gif" );
        Marker m = mapFragment.placeMarker(sävargården);
        ((InfoDetailActivity)getActivity()).siteMarkerMap.put(m, sävargården);


        /*
        ((MapFragment)mapFragment).googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
           public void onInfoWindowClick(Marker marker) {
                siteShortInfoMessage = siteMarkerMap.get(marker);
                Toast.makeText(InfoDetailActivity.this, "You choose " + siteShortInfoMessage.getName() + ". Description is " + siteShortInfoMessage.getDescription() + ".", Toast.LENGTH_SHORT).show();
                showLocationMessage(siteShortInfoMessage);
            }
        });  */

    }

}
