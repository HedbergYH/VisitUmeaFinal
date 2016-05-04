package com.example.umyhnystma.matsvisitumea;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener, OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    FragmentManager fm;
    FragmentTransaction trans;
    Map fragmentMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        trans = fm.beginTransaction();

        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        // Three tab to display in actionbar
        ab.addTab(ab.newTab().setText("Map").setTabListener(this));
        ab.addTab(ab.newTab().setText("Sites").setTabListener(this));
        ab.addTab(ab.newTab().setText("Search").setTabListener(this));

            /*
        mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
            */
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        //Called when a tab is selected
        int nTabSelected = tab.getPosition();
        switch (nTabSelected) {
            case 0:

                /* FUNGERAR EJ -TESTBIT

                if(fragmentMap==null){
                    fragmentMap = new Map();
                    trans.replace(R.id.container, fragmentMap).commit();
                }else{
                    trans.replace(R.id.container, fragmentMap);
                }

                */

                // FUNGERAR MEN KOPPLAR NOG DÅLIGT MOT FRAGMENT

                if(mapFragment == null){
                    mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                    //trans.add(R.id.container, mapFragment).addToBackStack(null).commit();
                }else{
                    //trans.replace(R.id.container, mapFragment).commit();
                    mapFragment.getMapAsync(this);
                }

                //setContentView(R.layout.activity_maps);
                break;
            case 1:
                setContentView(R.layout.fragment_fragment_sites);
                break;
            case 2:
                setContentView(R.layout.fragment_search);
                break;
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i("MIN_TAG","onMapReady mainactivity körs");

        mMap = googleMap;

        LatLng umea = new LatLng(63.826499, 20.2742188);

        mMap.addMarker(new MarkerOptions().position(umea).title("Marker at Folkuniversitetet"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(umea));


    }
}

