package com.example.umyhnystma.matsvisitumea;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener, OnMapReadyCallback, ToggleChange {

    private GoogleMap mMap;
    FragmentManager fm;
    FragmentTransaction trans;
    Fragment fragmentMap;
    Fragment siteListFragment;
    Fragment siteSearchFragment;
    Fragment toggleButtonsMainActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();


        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        // Three tab to display in actionbar
        ab.addTab(ab.newTab().setText("Map").setTabListener(this));
        ab.addTab(ab.newTab().setText("Sites").setTabListener(this));
        ab.addTab(ab.newTab().setText("Search").setTabListener(this));

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        //Called when a tab is selected
        int nTabSelected = tab.getPosition();
        switch (nTabSelected) {
            case 0:

                if (siteListFragment!=null){
                    trans = fm.beginTransaction();
                    trans.remove(siteListFragment).commit();
                }else if(siteSearchFragment!=null){
                    trans = fm.beginTransaction();
                    trans.remove(siteSearchFragment).commit();
                }

                trans = fm.beginTransaction();
                Log.i("MIN_TAG","case 0:");

                if(fragmentMap==null){
                    fragmentMap = new MapFragment();
                    trans.replace(R.id.mapContainer, fragmentMap).addToBackStack(null).commit();

                }else{
                    trans.replace(R.id.mapContainer, fragmentMap).addToBackStack(null).commit();
                }

                //Transaction for ToggleButtons fragment
                trans = fm.beginTransaction();

                if(toggleButtonsMainActivity == null){
                    //Set the toggle buttons below map
                    toggleButtonsMainActivity = new ToggleButtonsMainActivity();
                    trans.replace(R.id.container, toggleButtonsMainActivity).commit();
                }else{
                    trans.replace(R.id.container, toggleButtonsMainActivity).commit();
                }

                break;
            case 1:

                if(fragmentMap!=null){
                    trans = fm.beginTransaction();
                    trans.remove(fragmentMap).commit();
                }

                tabSiteSelected();
                Log.i("MIN_TAG","case 1:");
                break;
            case 2://Search

                if(fragmentMap!=null){
                    trans = fm.beginTransaction();
                    trans.remove(fragmentMap).commit();
                }

                tabSearchSelected();

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

    public void tabSiteSelected(){
        siteListFragment = new Sites();
        FragmentManager fm  = getSupportFragmentManager(); // hanterar fragment
        FragmentTransaction transaction = fm.beginTransaction(); // transfering av fragment

        transaction.replace(R.id.container, siteListFragment);                                               // fragmentSiteList är det nya fragmentet

        transaction.commit();
    }

    public void tabSearchSelected(){
        siteSearchFragment = new Search();
        FragmentManager fm  = getSupportFragmentManager(); // hanterar fragment
        FragmentTransaction transaction = fm.beginTransaction(); // transfering av fragment

         // container ligger i activity_main.xml
        transaction.replace(R.id.container, siteSearchFragment);                                               // fragmentSiteList är det nya fragmentet
        transaction.commit();




    }

    @Override
    public void onChangedGPS(int code, Boolean bool) {
        if(code == 1){
            //isChecked = false

            ((MapFragment)fragmentMap).GPS = bool;

            ((MapFragment)fragmentMap).checkGPSState();

        }else if(code == 2){
            //isChecked = true

            ((MapFragment)fragmentMap).GPS = bool;

            ((MapFragment)fragmentMap).checkGPSState();

        }
    }

    @Override
    public void onChangedLocationGuide(int code, Boolean bool) {

    }
}

