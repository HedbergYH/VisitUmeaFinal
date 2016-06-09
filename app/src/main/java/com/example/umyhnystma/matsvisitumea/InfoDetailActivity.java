package com.example.umyhnystma.matsvisitumea;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class InfoDetailActivity extends AppCompatActivity implements ActionBar.TabListener, LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    TextView myText;
    /***************************************
     * TA BORT ALLT NEDAN FÖR ATT UNDVIKA MERGEKONFLIKTER
     */

    private static final String RELIGIOUS = "Religious";
    private static final String HISTORICAL = "Historical";

    FragmentManager fm;
    FragmentTransaction trans;

    Fragment fragmentLocationMessage;

    Fragment fragment;
    MapFragment mapFragment;
    MapTrackFragment mapTrackFragment;


    Site siteShortInfoMessage;

    int mySelectedTab;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    Location mCurrentLocation;
    Fragment fragmentMap, listFragment, fragmentButton;

    RelativeLayout mapOrListContainer;

    protected HashMap<Marker, Site> siteMarkerMap;

    public static final String INTENT_TAB_NUMBER = "INTENT_NUMBER";

    ArrayList<Site> mySites;


    ActionBar ab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        mapOrListContainer = (RelativeLayout) findViewById(R.id.mapOrListContainer);

        Intent intent = getIntent();
        mySites = (ArrayList<Site>) intent.getSerializableExtra("MySites");



        siteMarkerMap = new HashMap<Marker, Site>();

        locationGetter();

        fm = getSupportFragmentManager();
        trans = fm.beginTransaction();


        fragmentMap = new MapFragment();
        listFragment = new ListFragment();
        fragmentLocationMessage = new LocationMessage();

        invokeMapFragment();
        invokeListFragment();

        trans = fm.beginTransaction();
        trans.hide(fragmentMap).commit();
        ab = getSupportActionBar();
        setTabBars();




    }

    public void setTabBars() {

        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.addTab(ab.newTab().setText("List").setTabListener(this));
        ab.addTab(ab.newTab().setText("Map").setTabListener(this));

    }

    public void showTabBars() {
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    public void hideTabBars() {
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }


    public void invokeMapFragment() {
        trans = fm.beginTransaction();
        trans.add(R.id.mapOrListContainer, fragmentMap).commit();

    }


    public void invokeListFragment() {
        trans = fm.beginTransaction();
        trans.add(R.id.mapOrListContainer, listFragment).commit();
    }


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            this.finish();
        }
        else if(mapTrackFragment != null && mapTrackFragment.isVisible())
        {
            getSupportFragmentManager().popBackStack();
        }

    else if(listFragment.getFragmentManager().findFragmentByTag("DETAIL_INFO_FRAGMENT_FROM_LIST")!=null&&listFragment.getFragmentManager().findFragmentByTag("DETAIL_INFO_FRAGMENT_FROM_LIST").isVisible())
    {
        Log.i("MIN_TAG", "infodetailfrag syns");
        showTabBars();
        trans = fm.beginTransaction();
        trans.show(listFragment).commit();
        getSupportFragmentManager().popBackStack();

    }

    else if(fragmentLocationMessage.getFragmentManager().findFragmentByTag("DETAIL_INFO_FRAGMENT_FROM_MAP")!=null&&fragmentLocationMessage.getFragmentManager().findFragmentByTag("DETAIL_INFO_FRAGMENT_FROM_MAP").isVisible())

        {
            Log.i("MIN_TAG", "infoDetailFrag finns ifrån locationMessage");
            showTabBars();
            trans = fm.beginTransaction();
            trans.show(fragmentMap).commit();
            getSupportFragmentManager().popBackStack();
        }

    else
    {
        Log.i("MIN_TAG", "sista else, popBackStack()ska köras");
        getSupportFragmentManager().popBackStack();
    }

}





    @Override
    public void onStart(){
        super.onStart();

        setUpSites();
        mGoogleApiClient.connect(); // connectar mot mot Google-maps för att erhålla telefonens position
    }

    protected void setUpSites() {
        for(int i = 0; i < mySites.size(); i++){
            Marker m = ((MapFragment)fragmentMap).placeMarker(mySites.get(i));
            siteMarkerMap.put(m, mySites.get(i));
        }

        ((MapFragment)fragmentMap).googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                siteShortInfoMessage = siteMarkerMap.get(marker);
                Toast.makeText(InfoDetailActivity.this, "You choose " + siteShortInfoMessage.getName() + ". Description is " + siteShortInfoMessage.getDescription() + ".", Toast.LENGTH_SHORT).show();
                showLocationMessage(siteShortInfoMessage);
            }
        });
    }

    public void showMapTracFrag(Site mySelectedSite){
        Bundle bundle = new Bundle();
        bundle.putSerializable("KEY_SERIALIZABLE", mySelectedSite );

        mapTrackFragment = new MapTrackFragment();
        fm = getSupportFragmentManager();
        trans =fm.beginTransaction();
        mapTrackFragment.setArguments(bundle);
        trans.add(R.id.activity_info_detail_relroot_container, mapTrackFragment); // funkar
        trans.addToBackStack("MAP_TRACK_FRAGMENT_MAP");
        trans.commit();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    public void showLocationMessage(Site s){

        fm = getSupportFragmentManager();
        trans = fm.beginTransaction();
        trans.add(R.id.messageContainer, fragmentLocationMessage).addToBackStack(null).commit();


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
        mRequest.setInterval(20000);
        mRequest.setFastestInterval(5000);
        mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }


    @Override
    public void onLocationChanged(Location location) {

        mCurrentLocation = location;
        locationUpdate();

    }

    public void locationUpdate() {

        ((MapFragment) fragmentMap).latitude = mCurrentLocation.getLatitude();
        ((MapFragment) fragmentMap).longitude = mCurrentLocation.getLongitude();
        ((MapFragment) fragmentMap).setMyLocationSitesMap();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle){

    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

    } else {
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        mySelectedTab = tab.getPosition();

        switch (mySelectedTab){

            case 0:
                Log.i("TAG", "mySelectedTab (case 0) är"+ mySelectedTab);
                if(fragmentMap.isVisible()){
                    Log.i("TAG", "ListFragment is hidden and exists");
                    trans = fm.beginTransaction();
                    trans.hide(fragmentMap).commit();
                }
                trans = fm.beginTransaction();
                trans.show(listFragment).commit();
            break;
            case 1:
                Log.i("TAG", "mySelectedTab(case 1) är"+ mySelectedTab);
                if(listFragment.isVisible()){

                    Log.i("TAG", "ListFragment is visible and exists");
                    trans = fm.beginTransaction();
                    trans.hide(listFragment).commit();

                }
                trans = fm.beginTransaction();
                trans.show(fragmentMap).commit();
            break;
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
