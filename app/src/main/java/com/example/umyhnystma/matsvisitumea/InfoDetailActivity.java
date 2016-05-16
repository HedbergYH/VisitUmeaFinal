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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class InfoDetailActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ToggleChange {

    TextView myText;
    /***************************************
     * TA BORT ALLT NEDAN FÖR ATT UNDVIKA MERGEKONFLIKTER
     *
     */
    FragmentManager fm;
    FragmentTransaction trans;
    Fragment fragment;


    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    Location mCurrentLocation;
    Fragment fragmentMap,listFragment,fragmentButton;

    public static final String INTENT_TAB_NUMBER = "INTENT_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);

        // för knappfragment
        fm = getSupportFragmentManager();
        trans = fm.beginTransaction();
        fragmentButton = new ButtonFragment();

        trans.add(R.id.buttonContainer, fragmentButton).commit(); // tar fram knappfragmentet

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

        listFragment = new ListFragment();



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
        trans.replace(R.id.mapOrListContainer, fragmentMap).commit();
    }


    public void invokeListFragment(){
        trans = fm.beginTransaction();
        trans.replace(R.id.mapOrListContainer, listFragment).commit();
    }









    public void onBackPressed(){

      //  Intent intent = new Intent(getActivity(), InfoDetailActivity.class);     // Anropar under runtime class-filen
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(INTENT_NOTE_STRING, currentNote.note);                 // Sträng skickas med bundle
        intent.putExtra(INTENT_TAB_NUMBER, 1);                         // Position skickas med bundle
        this.startActivity(intent);



        Log.i("MIN_TAG", "onBackPressed i infoDetailActivity");
        super.onBackPressed();
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
    public void onChangedGPS(int code, Boolean bool) {
        //Ett tomt interface som kollar om GPS är på eller av i MainActivity
        //Finns här för att kunna använda samma MapFragment-klass
    }

    @Override
    public void onChangedLocationGuide(int code, Boolean bool) {
        //Ett tomt interface som kollar om GPS är på eller av i MainActivity
        //Finns här för att kunna använda samma MapFragment-klass
    }
}
