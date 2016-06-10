package com.example.umyhnystma.matsvisitumea;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.content.Intent;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements ResultCallback, ActionBar.TabListener, LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    // med Geofence som kanske funkar/Mats 05-24
    FragmentManager fm;
    FragmentTransaction trans;
    Fragment currentFragment;
    Fragment fragmentMap;
    Fragment siteListFragment;
    Fragment siteSearchFragment;
   // Fragment toggleButtonsMainActivity; BORTTAGET TILLS VIDARE
    DetailInfoFragment detailInfoFragment;
    int tabChoosen;
    int nTabSelected;


    Intent intent; // intent skapat från InfoDetailActivity

    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    Location mCurrentLocation;

    private static final String RELIGIOUS = "Religious";
    private static final String HISTORICAL = "Historical";
    private static final String CULTURAL = "Cultural";
    private static final String SAVED = "KEY_SAVED_SITES";
    private static final String SAVED_SITES = "SAVED_SITES";

    //public static final String INTENT_TAB_NUMBER = "INTENT_NUMBER";

    Gson gson;


///////////////////för Geofence - nedan /////////////////////////////
    private boolean mGeofencesAdded;

    List<Geofence> mGeofenceList = new ArrayList<>();
    PendingIntent mGeofencePendingIntent;
///////////////////för Geofence - ovan /////////////////////////////


    SavedSitesGson mySites = new SavedSitesGson();

    /**
     * Used to persist application state about whether geofences were added.
     */
    //private SharedPreferences mSharedPreferences;

    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;



    // Retrieve an instance of the SharedPreferences object.





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

///////////////////för Geofence - nedan /////////////////////////////
//        mGeofencePendingIntent = null;
///////////////////för Geofence - ovan /////////////////////////////

        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);

        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
///////////////////för Geofence - nedan /////////////////////////////
//       mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);
///////////////////för Geofence - ovan /////////////////////////////



        checkInternetState();

        checkGPSstate();

        fm = getSupportFragmentManager();
        detailInfoFragment = new DetailInfoFragment();

        loadSites();

        //intent = getIntent();
       // tabChoosen = intent.getIntExtra(InfoDetailActivity.INTENT_TAB_NUMBER, 0);
        Log.i("MIN_TAG", "tabChoosen: " + tabChoosen);

        ActionBar ab = getSupportActionBar();


        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        locationGetter();


        // Three tab to display in actionbar
        ab.addTab(ab.newTab().setText("Map").setTabListener(this));
        ab.addTab(ab.newTab().setText("Sites").setTabListener(this));
        ab.addTab(ab.newTab().setText("Search").setTabListener(this));

    }

//////////////////////////// GeoFences - nedan /////////////////////////////////////////
/*
    private void addGeofences() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }



    private void removeGeofences(){
        LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient,
                // This is the same pending intent that was used in addGeofences().
                getGeofencePendingIntent()
        ).setResultCallback(this); // Result processed in onResult().
    }


 // Mats: original   private void setUpGeofence() {
 public void populateGeofenceList() {
     for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()) {

         mGeofenceList.add(new Geofence.Builder()     //mGeofenceList är en arraylist med Geofenceobjekt
                 // Set the request ID of the geofence. This is a string to identify this
                 // geofence.


                 .setRequestId(entry.getKey())

                 .setCircularRegion(
                         entry.getValue().latitude,
                         entry.getValue().longitude,
                         200
                 )
                 .setLoiteringDelay(10000)
                 .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                 // .setExpirationDuration(SyncStateContract.Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                 .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                         Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                 .build());
         Log.i("MIN_TAG", "mGeofenceList.size: "+ mGeofenceList.size());
     }
 }



    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
        //    setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
         //   Log.e(TAG, errorMessage);
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }




        private PendingIntent getGeofencePendingIntent() {
            // Reuse the PendingIntent if we already have it.
            if (mGeofencePendingIntent != null) {
                return mGeofencePendingIntent;
            }
            Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
            // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
            // calling addGeofences() and removeGeofences().
            return PendingIntent.getService(this, 0, intent, PendingIntent.
                    FLAG_UPDATE_CURRENT);
        }

*/
//////////////////////////// GeoFences - ovan /////////////////////////////////////////



    private void checkInternetState() {

        if(isNetworkAvailable()==true){

            Log.i("TAG", "Nätverk tillgängligt");
        }else {
            Toast.makeText(MainActivity.this, "Enable internet connection to use the app.", Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Network is disabled in your device. Would you like to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Go to network settings.",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callNetworkSettingIntent = new Intent(
                                            Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(callNetworkSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

    }


// Check all connectivities whether available or not
        public boolean isNetworkAvailable() {
            ConnectivityManager cm = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            // if no network is available networkInfo will be null
            // otherwise check if we are connected
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
            return false;
        }

    private void checkGPSstate() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Go to settings to enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("KEY",false); // för att navigationsknapparna INTE ska visas i kart-fragmentet
                    fragmentMap.setArguments(bundle);
                    trans.replace(R.id.mapContainer, fragmentMap).addToBackStack(null).commit();

                } else {
                    trans.replace(R.id.mapContainer, fragmentMap).addToBackStack(null).commit();
                }


                /*

                TOGGLEBUTTON FÖR PUSH NOTIS OCH LIKNANDE

                //Transaction for ToggleButtons fragmentLocationMessage
                trans = fm.beginTransaction();


                if (toggleButtonsMainActivity == null) {
                    //Set the toggle buttons below map
                    toggleButtonsMainActivity = new ToggleButtonsMainActivity();
                    trans.replace(R.id.GPScontainer, toggleButtonsMainActivity).commit();
                } else {

                    trans.replace(R.id.GPScontainer, toggleButtonsMainActivity).commit();
                }

                */


                break;

            case 1:

                if (fragmentMap != null) {
                    trans = fm.beginTransaction();
                    trans.remove(fragmentMap).commit();

                    /*

                    TOGGLE BUTTON PUSH NOTIS
                    trans = fm.beginTransaction();
                    trans.remove(toggleButtonsMainActivity).commit();

                    */
                }

                tabSiteSelected();
                Log.i("MIN_TAG", "case 1:");
                break;
            case 2://Search

                if (fragmentMap != null) {
                    trans = fm.beginTransaction();
                    trans.remove(fragmentMap).commit();

                    /*

                    TOGGLE BUTTON PUSH NOTIS

                    trans = fm.beginTransaction();
                    trans.remove(toggleButtonsMainActivity).commit();

                    */
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
        FragmentManager fm = getSupportFragmentManager(); // hanterar fragmentLocationMessage
        FragmentTransaction transaction = fm.beginTransaction(); // transfering av fragmentLocationMessage

        transaction.replace(R.id.container, siteListFragment);                                               // fragmentSiteList är det nya fragmentet

        transaction.commit();

    }

    public void tabSearchSelected() {
        siteSearchFragment = new Search();
        FragmentManager fm = getSupportFragmentManager(); // hanterar fragmentLocationMessage
        FragmentTransaction transaction = fm.beginTransaction(); // transfering av fragmentLocationMessage

        // container ligger i activity_main.xml
        transaction.replace(R.id.container, siteSearchFragment);                                               // fragmentSiteList är det nya fragmentet
        transaction.commit();

    }



    protected synchronized void locationGetter() {
        //////////////////////////// APIClient - nedan /////////////////////////////////////////
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //Hur ofta GPS-koordinaterna ska uppdateras. OnLocationChanged sätter ut platsen på kartan.

        mRequest = new LocationRequest();
        mRequest.setInterval(20000);
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
///////////////////för Geofence - nedan /////////////////////////////
//          populateGeofenceList();
//          addGeofences();
///////////////////för Geofence - ovan /////////////////////////////

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
        ((MapFragment) fragmentMap).setMyLocationMainMap();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.i("MIN_TAG", "connectionResult" + connectionResult);

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
    public void onResume() {
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

    private void createAndSaveSitesOnce(){
        //Creating and setting up all site-objects with gson-json;

        // Cultural
        Site väven = new Site("Väven", "Kulturellt centrum i Umeå med scener, biografer, bibliotek och utsällningar", 63.8244777, 20.259454300000016, CULTURAL,"http://static1.squarespace.com/static/55738760e4b07ff6e5767faa/t/55f88c0be4b0c80742d398c1/1442352163898/" );
        Site saraLidmanTunneln = new Site("Sara Lidman-tunneln", "Uppförd 2013. Väggarna pryds av citat av västerbottniska Sara Lidman och här hörs fågelsång året om.", 63.82965, 20.265826, CULTURAL,"http://blogg.vk.se/wp-content/uploads/oldblog/1589/images/saratunneln8(1).JPG");

       // Religiuos
        Site backensKyrka = new Site("Backens kyrka", "Backens kyrka är en annan historia än allt annat",63.8380731,20.1563725, RELIGIOUS,"http://www.umea.se/images/18.73474df7141ec1b19d15615/1383648352953/Backens_kyrka_h.gif");
        Site umeåStadsKyrka = new Site("Umeå stadskyrka", "Stort och fint hus åt jesus", 63.823552, 20.267803, RELIGIOUS,"https://c1.staticflickr.com/5/4046/4613453840_de7b53beeb.jpg");
        Site holmsundsKyrka = new Site("Holmsunds kyrka", "Holmsunds största gudsbygge",63.703577, 20.350006, RELIGIOUS,"http://www.umea.se/images/18.73474df7141ec1b19d14db7/1383570139256/Holmsunds-kyrka_h.gif");

        // Historical
        Site sävargården = new Site("Sävargården", "Fint hus det här", 63.828462,20.290932, HISTORICAL,"http://www.umea.se/images/18.73474df7141ec1b19d14e9f/1383576061560/S%C3%A4varg%C3%A5rden_h.gif" );
        Site olofsforsBruk = new Site("Olofsfors bruk", "WOW, just WOW", 63.58058699999999, 19.441219000000046, HISTORICAL,"http://olofsforsbruk.nu/wp-content/uploads/2012/06/masugn_web-604x320.jpg");
        Site cellFängelset = new Site("Gamla Fängelset", "Lätt att klättra över muren", 63.822337, 20.275396, HISTORICAL,"https://exp.cdn-hotels.com/hotels/8000000/7830000/7827400/7827340/7827340_13_y.jpg");
        Site baggböleHerrgård = new Site("Baggböle herrgård", "Fint och god mat", 63.84032999999999, 20.117009999999937, HISTORICAL,"http://www.umea.se/images/18.73474df7141ec1b19d14d79/1383568853404/Baggb%C3%B6le-Herrg%C3%A5rd_h.jpg");
        Site rådhuset = new Site("Rådhuset", "Umeås rådhus.", 63.825337, 20.262847, HISTORICAL,"http://www.umea.se/images/18.53d383fe142675f74e6c61/1384780829004/Radhuset_liten_h.gif");
        Site norrbyskär = new Site("Norrbyskär", "Gammal flottarort ute i skärgården", 63.55271999999999, 19.876070000000027, HISTORICAL,"http://www.umea.se/images/18.73474df7141ec1b19d14e24/1383571793773/Norrbyskar_1_h.jpg");


        mySites.allSites.add(backensKyrka);
        mySites.allSites.add(umeåStadsKyrka);
        mySites.allSites.add(holmsundsKyrka);
        mySites.allSites.add(sävargården);
        mySites.allSites.add(olofsforsBruk);
        mySites.allSites.add(cellFängelset);
        mySites.allSites.add(baggböleHerrgård);
        mySites.allSites.add(rådhuset);
        mySites.allSites.add(norrbyskär);
        mySites.allSites.add(väven);
        mySites.allSites.add(saraLidmanTunneln);

        Collections.sort(mySites.allSites, Site.COMPARE_BY_BUILDING_NAME);// Sorterar byggnaderna i bokstavsordning


        SharedPreferences preferences = getSharedPreferences(SAVED,0);
        SharedPreferences.Editor editor = preferences.edit();
        gson = new Gson();

        String savedString = gson.toJson(mySites);
        editor.putString(SAVED_SITES, savedString);
        editor.commit();




    }
    private void loadSites(){
        //Loading all site-objects with gson-json;

        gson = new Gson();

        SharedPreferences preferences = getSharedPreferences(SAVED, 0);
        String loadedString = preferences.getString(SAVED_SITES, null);

        if(loadedString == null){

            mySites = new SavedSitesGson();
            createAndSaveSitesOnce();

        }else{
            Log.i("TAG", "i load(); allSites = " + mySites.toString());
            mySites = gson.fromJson(loadedString, SavedSitesGson.class);

        }

        for(int i = 0; i < mySites.allSites.size(); i++){

            if(mySites.allSites.get(i).category.equals(CULTURAL)){
                //CULTURAL
                //Lägger till objektet i cultural-arraylisten
                mySites.culturalSites.add(mySites.allSites.get(i));

            }else if(mySites.allSites.get(i).category.equals(HISTORICAL)){
                //HISTORICAL
                //Lägger till objektet i historical-arraylisten
                mySites.historicalSites.add(mySites.allSites.get(i));

            }else{
                //RELIGIOUS
                //Lägger till objektet i religious-arraylisten
                mySites.religiousSites.add(mySites.allSites.get(i));
            }

        }

    }
    public void fragmentCheckGPS(){
        checkGPSstate();
    }


    @Override
    public void onResult(@NonNull Result result) {

    }



    @Override
    public void onBackPressed(){
        Log.i("MIN_TAG", "onBackPressed i MainActivity");
        currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.container);        // Hämtar referens till Sites-fragment
        if (currentFragment == null)
            currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.mapContainer);// Hämtar referens till Map-fragment

        Log.i("MIN_TAG", "currentFragment" + currentFragment.toString());

        if (currentFragment instanceof Sites ) {
            Log.i("MIN_TAG", "onBackPressed i MainActivity, currentFragment instanceof SitesFragment");
            this.finish();
        }

        if (currentFragment instanceof MapFragment ){
            Log.i("MIN_TAG", "onBackPressed i MainActivity, currentFragment instanceof MapFragmen");
            this.finish();                                               // Dödar mainActivity
        }

        if(currentFragment instanceof Search){
            this.finish();
        }

        if(currentFragment instanceof DetailInfoFragment){
            getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            getSupportFragmentManager().popBackStack();
            trans = fm.beginTransaction();
            trans.show(siteSearchFragment).commit();

        }

    }

    public void showInfoDetailFragmentForSite(Site site){

        trans = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("KEY_SERIALIZABLE",site);
        detailInfoFragment.setArguments(bundle);
        trans.add(R.id.container, detailInfoFragment).addToBackStack(null).commit();
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        trans = fm.beginTransaction();
        trans.hide(siteSearchFragment).commit();
    }



}



