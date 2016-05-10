package com.example.umyhnystma.matsvisitumea;


import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener, ToggleChange, com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    // private GoogleMap mMap; Kanske kan man ta bort det här
    FragmentManager fm;
    FragmentTransaction trans;
    Fragment fragmentMap;
    Fragment siteListFragment;
    Fragment siteSearchFragment;
    Fragment toggleButtonsMainActivity;
    int tabChoosen;
    int nTabSelected;

    Intent intent; // intent skapat från InfoDetailActivity

    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();


        intent = getIntent();
        tabChoosen = intent.getIntExtra(InfoDetailActivity.INTENT_TAB_NUMBER, 0);

        Log.i("MIN_TAG", "tabChoosen: " + tabChoosen);

        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        locationGetter();


        // Three tab to display in actionbar
        ab.addTab(ab.newTab().setText("Map").setTabListener(this));
        ab.addTab(ab.newTab().setText("Sites").setTabListener(this));
        ab.addTab(ab.newTab().setText("Search").setTabListener(this));

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        //Called when a tab is selected
        nTabSelected = tab.getPosition();
        //     if  (tabChoosen != 1)
        //     else
        //         nTabSelected = 1;


        switch (nTabSelected) {
            case 0:

                if (siteListFragment != null) {
                    trans = fm.beginTransaction();
                    trans.remove(siteListFragment).commit();
                } else if (siteSearchFragment != null) {
                    trans = fm.beginTransaction();
                    trans.remove(siteSearchFragment).commit();
                }

                trans = fm.beginTransaction();
                Log.i("MIN_TAG", "case 0:");

                if (fragmentMap == null) {
                    fragmentMap = new MapFragment();
                    trans.replace(R.id.mapContainer, fragmentMap).addToBackStack(null).commit();

                } else {
                    trans.replace(R.id.mapContainer, fragmentMap).addToBackStack(null).commit();
                }

                //Transaction for ToggleButtons fragment
                trans = fm.beginTransaction();


                if (toggleButtonsMainActivity == null) {
                    //Set the toggle buttons below map
                    toggleButtonsMainActivity = new ToggleButtonsMainActivity();
                    trans.replace(R.id.container, toggleButtonsMainActivity).commit();
                } else {

                    trans.replace(R.id.container, toggleButtonsMainActivity).commit();
                }


                break;

            case 1:

                if (fragmentMap != null) {
                    trans = fm.beginTransaction();
                    trans.remove(fragmentMap).commit();
                }

                tabSiteSelected();
                Log.i("MIN_TAG", "case 1:");
                break;
            case 2://Search

                if (fragmentMap != null) {
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


    public void tabSiteSelected() {

        Log.i("MIN_TAG", "i Main, i tabSiteSelected()");
        siteListFragment = new Sites();
        FragmentManager fm = getSupportFragmentManager(); // hanterar fragment
        FragmentTransaction transaction = fm.beginTransaction(); // transfering av fragment

        transaction.replace(R.id.container, siteListFragment);                                               // fragmentSiteList är det nya fragmentet

        transaction.commit();

    }

    public void tabSearchSelected() {
        siteSearchFragment = new Search();
        FragmentManager fm = getSupportFragmentManager(); // hanterar fragment
        FragmentTransaction transaction = fm.beginTransaction(); // transfering av fragment

        // container ligger i activity_main.xml
        transaction.replace(R.id.container, siteSearchFragment);                                               // fragmentSiteList är det nya fragmentet
        transaction.commit();


    }

    @Override
    public void onChangedGPS(int code, Boolean bool) {
        if (code == 1) {
            //isChecked = false

            ((MapFragment) fragmentMap).GPS = bool;

            ((MapFragment) fragmentMap).checkGPSState();

        } else if (code == 2) {
            //isChecked = true

            ((MapFragment) fragmentMap).GPS = bool;

            ((MapFragment) fragmentMap).checkGPSState();

        }
    }

    @Override
    public void onChangedLocationGuide(int code, Boolean bool) {

    }

    public void locationGetter() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mRequest = new LocationRequest();
        mRequest.setInterval(10000);
        mRequest.setFastestInterval(5000);
        mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.i("MIN_TAG", "onConnected.");


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            Log.i("MIN_TAG", "onConnected : if");

        } else {
            Log.i("MIN_TAG", "onConnected : else");
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mRequest, this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("MIN_TAG", "onLocationChanged. Location = Lat: " + location.getLatitude() + ", Long:" + location.getLongitude());

        mCurrentLocation = location;
        locationUpdate();

    }

    public void locationUpdate() {

        ((MapFragment) fragmentMap).latitude = mCurrentLocation.getLatitude();
        ((MapFragment) fragmentMap).longitude = mCurrentLocation.getLongitude();
        ((MapFragment) fragmentMap).setMyLocation();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MIN_TAG", "onStart");

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MIN_TAG", "onStop");

        mGoogleApiClient.disconnect();

    }

        @Override
        public void onResume () {
            super.onResume();
            Log.i("MIN_TAG", "onResume i MainActivity");

        }
        @Override
        public void onPause() {
            super.onPause();
            Log.i("MIN_TAG", "onPause i MainActivity");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.i("MIN_TAG", "onDestroy i MainActivity");

        }

}



