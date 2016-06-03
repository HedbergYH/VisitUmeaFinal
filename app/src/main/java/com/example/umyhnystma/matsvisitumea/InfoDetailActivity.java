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
import android.graphics.Color;
import android.graphics.Rect;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoDetailActivity extends AppCompatActivity implements ActionBar.TabListener, LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    TextView myText;
    /***************************************
     * TA BORT ALLT NEDAN FÖR ATT UNDVIKA MERGEKONFLIKTER
     *
     */

    private static final String RELIGIOUS = "Religious";
    private static final String HISTORICAL = "Historical";

    FragmentManager fm;
    FragmentTransaction trans;
    Fragment fragment;

    Site siteShortInfoMessage;

    int mySelectedTab;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    Location mCurrentLocation;
    Fragment fragmentMap,listFragment,fragmentButton;

    private HashMap <Marker, Site> siteMarkerMap;

    public static final String INTENT_TAB_NUMBER = "INTENT_NUMBER";

    ArrayList<Site> mySites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);


        Intent intent = getIntent();
        mySites = (ArrayList<Site>) intent.getSerializableExtra("MySites");

/*
        // för knappfragment
        fm = getSupportFragmentManager();
        trans = fm.beginTransaction();
        fragmentButton = new ButtonFragment();

        trans.add(R.id.buttonContainer, fragmentButton).commit(); // tar fram knappfragmentet

*/


        siteMarkerMap = new HashMap<Marker, Site>();

        locationGetter();

        fm = getSupportFragmentManager();
        trans = fm.beginTransaction();

        fragmentMap = new MapFragment();
        listFragment = new ListFragment();

        invokeMapFragment();
        invokeListFragment();

        trans = fm.beginTransaction();
        trans.hide(fragmentMap).commit();

        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.addTab(ab.newTab().setText("List").setTabListener(this));
        ab.addTab(ab.newTab().setText("Map").setTabListener(this));


/*
        Intent intent = getIntent();
        int extras = intent.getExtras().getInt("KEY");

        locationGetter();

        fm = getSupportFragmentManager();
        trans = fm.beginTransaction();
        fragmentMap = new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("KEY", extras);

        fragmentMap.setArguments(bundle);
        trans.add(R.id.mapOrListContainer, fragmentMap).commit();
*/
    }



    public void invokeMapFragment(){
        trans = fm.beginTransaction();
        trans.add(R.id.mapOrListContainer, fragmentMap).commit();

    }


    public void invokeListFragment(){
        trans = fm.beginTransaction();
        trans.add(R.id.mapOrListContainer, listFragment).commit();
    }

    @Override
    public void onBackPressed(){
        Log.i("MIN_TAG", "onBackPressed i infoDetailActivity");
            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {

                //  Intent intent = new Intent(getActivity(), InfoDetailActivity.class);     // Anropar under runtime class-filen
                Intent intent = new Intent(this, MainActivity.class);
                //intent.putExtra(INTENT_NOTE_STRING, currentNote.note);                 // Sträng skickas med bundle
                intent.putExtra(INTENT_TAB_NUMBER, 1);                         // Position skickas med bundle
                this.startActivity(intent);
                //onBackPressed();

            } else {
                getSupportFragmentManager().popBackStack();
            }

    }

    @Override
    public void onStart(){
        super.onStart();

        setUpSites();
    }
    private void setUpSites() {

        for(int i = 0; i < mySites.size(); i++){
            Marker m = ((MapFragment)fragmentMap).placeMarker(mySites.get(i));
            siteMarkerMap.put(m, mySites.get(i));
        }
        /*
        Site backensKyrka = new Site("Backens kyrka", "Backens kyrka är en annan historia än allt annat",63.8380731,20.1563725, RELIGIOUS);
        Site sävarGården = new Site ("Sävargården", "Information om Sävargården", 63.8284222, 20.290917, HISTORICAL);

        Marker backensMarker = ((MapFragment)fragmentMap).placeMarker(backensKyrka);
        Marker sävarMarker = ((MapFragment)fragmentMap).placeMarker(sävarGården);


        siteMarkerMap.put(backensMarker, backensKyrka);
        siteMarkerMap.put(sävarMarker, sävarGården);

        */

        ((MapFragment)fragmentMap).googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                siteShortInfoMessage = siteMarkerMap.get(marker);
                Toast.makeText(InfoDetailActivity.this, "You choose " + siteShortInfoMessage.getName() + ". Description is " + siteShortInfoMessage.getDescription() + ".", Toast.LENGTH_SHORT).show();
                showLocationMessage(siteShortInfoMessage);
            }
        });
    }


    @Override
    public void onResume(){
        super.onResume();

        Log.i("MIN_TAG","onResume i infoDetailActivity");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("MIN_TAG","onPause i infoDetailActivity");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MIN_TAG","onDestroy i infoDetailActivity");
    }

    public void showLocationMessage(Site s){


        /*****************
         * TESTAR ATT KÖRA MITT MESSAGEFRAGMENT
         * TA BORT NEDAN
         */

        fm = getSupportFragmentManager();
        fragment = new LocationMessage();
        trans = fm.beginTransaction();
        trans.add(R.id.messageContainer, fragment).addToBackStack(null).commit();


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
    public void onConnected(@Nullable Bundle bundle){

        Log.i("MIN_TAG", "onConnected. I InfoDetailActivity");

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
