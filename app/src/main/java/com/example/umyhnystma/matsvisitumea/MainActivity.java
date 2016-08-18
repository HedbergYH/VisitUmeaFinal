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
        ab.addTab(ab.newTab().setText("Sites").setTabListener(this));
        ab.addTab(ab.newTab().setText("Map").setTabListener(this));
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
            case 1:

                if (siteListFragment != null) {
                    trans = fm.beginTransaction();
                    trans.remove(siteListFragment).commit();
                } else if (siteSearchFragment != null) {
                    trans = fm.beginTransaction();
                    trans.remove(siteSearchFragment).commit();
                }

                trans = fm.beginTransaction();
                Log.i("MIN_TAG", "case 1:");

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

            case 0:

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
                Log.i("MIN_TAG", "case 0:");
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


        if(fragmentMap != null){

            ((MapFragment) fragmentMap).latitude = mCurrentLocation.getLatitude();
            ((MapFragment) fragmentMap).longitude = mCurrentLocation.getLongitude();
            ((MapFragment) fragmentMap).setMyLocationMainMap();
        }

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

        //Här under kan vi lägga in fler provisoriska Sites inför visningen i Skellefteå

        Site bonnstan = new Site("Bonnstan", "Bonnstan är skelleftebornas namn på sin kyrkstad med anor från 1600-talet. Kanske är den allra bäst bevarade kyrkstaden i norr när man inte låtit elektrifiera den och därmed behållit dess själ. \n" +
                "\n" +
                "Flera av timmerbyggnaderna är mer än 150 år gamla och rymmmer 116 kyrkstadshus med sammanlagt 392 kamrar. Bonnstan är en levande kyrkstad och sommartid är det många som använder sina kamrar som utflyktsmål. Dessutom arrangeras här varje sommar uppskattade evenemang för både stora och små. Några av de rödaste av sommarens smultron i Skellefteå hittar man definitivt i Bonnstan och kanske framför allt under kyrkhelgen och det traditionella midsommarfirandet. \n" +
                "\n" +
                "Kyrkoplikten och det vida landskapet Redan på 1600-talet myllrade det här av liv. På grund av det glest befolkade landskapet i norr och den kyrkoplikt som tvang dåtidens människor till kyrkan kom kyrkstäder att anläggas runt om i norrland. De kyrkopliktiga landsortsborna kom således att samlades i Bonnstan under kyrkohögtiderna och då bedrevs kommers, det söp, slogs, friades, bad om Guds nåd och lyssnade till prästers förmaningar. Och just här skrevs faktiskt också de första raderna i Skellefteå stads historia.",64.7506479,20.9326458, HISTORICAL, "http://visitskelleftea.se/wp-content/uploads/2016/08/585a64a078cd4c11b38a2a019a27998d-460x336.jpeg");
        Site fyren = new Site("Fyren på Bjuröklubb", "Vid fyren på Bjuröklubb har man en fantastisk utsikt mot havet och ut över naturreservatet. Här finns en utsiktsramp som gör att alla, även rörelsehindrade och rullstolsburna, har möjlighet att besöka Bjuröklubbs Fyrplats för att uppleva en storslagen naturupplevelse. Fyrhuset med boendemöjligheter kan hyras året om. Sommarcafé.",64.480779, 21.575055, CULTURAL, "http://visitskelleftea.se/wp-content/uploads/2016/06/5c14a3eec71ec949a977712d09076661-460x336.jpeg");

        Site bodaBorg = new Site ("Boda Borg", "Mitt inne i Vitberget, bara några kilometer norr om centrum, finns ett äventyrshus för alla åldrar. En ombyggd stridsledningscentral skapar en mystisk inramning till de 51 rum som rymmer utmaningar och spännande äventyr. Boda Borg är lika lämpligt för barnfamiljen, svensexan och 50-årsfesten som för olika former av teambuilding och företagsevenemang.", 64.7686993,20.9756357, CULTURAL, "http://pictures.infoskelleftea.se/foretagsbilder/244332161nsmail-1.jpg");


        /*
        // Cultural
        Site sparken = new Site("Sparken", "Skateparken Sparken är den första delen av projektet Staden mellan broarna som färdigställts. Arbetet startade våren 2008 efter en dialog mellan Umeå kommun och aktiva föreningar för unga i Umeå, en dialog som satt sin prägel såväl på hela platsens utformning såväl som på använda material, tekniska lösningar och skötsel av ytorna.\n" +
                "Skisserna till Sparken togs fram av skateföreningen och vidarebearbetades senare av det kanadensiska projekterings- och designkontoret Newline. Sparken designades för att i första hand erbjuda streetskate i stadslika miljöer med ramper, trappor, bänkar etc. I parkens västra del finns också en pool med två olika djup för att erbjuda åkning av olika svårighetsgrad. Sparken är systematiskt utformad i tre delar för att ge möjligheter för såväl erfarna som nybörjare att utveckla sina åktekniker.\n" +
                "Sparkens centrala placering i staden, på en före detta parkering vid Tegsbrons landfäste med den urbana, lite ruffa och bullriga miljön ger den storstadskaraktär som efterfrågades. Parkens gestaltning och framförallt anpassning till omgivande miljöer har varit viktig.\n" +
                "Skateparken består till allra största del av platsgjuten betong men vertikala ytor t ex murar har byggts av tegel. För att färg och struktur ska likna omgivande fasader har återanvänd tegelsten använts. \n" +
                "Den infällda belysningen har, precis som under bron, en framträdande karaktär. Stolpar med spotlightarmaturer och LED-belysning i olika färger förhöjer effekten i parken.", 63.825472, 20.254096, CULTURAL, "http://www.umea.se/images/18.5df068071445d1463ca1e87/1393426397655/sparken_webb.gif");
        Site norra_skenet = new Site ("Norra skenet", "As part of the planning of the Umeå University campus, a competition for a sculpture was held 1967 and won by Ernst Nordin. Norra skenet was raised 1969 at the campus and moved to the actual location close to the University Dam in 1995, due to the construction of the Teachers' Training Hall.\n" +
                "\n" +
                "The sculpture is made of polished stainless steel. Rectangular steel pipes have been welded together in a diagonal composition, resembling Aurora Borealis (Northern Lights). The structure is lit by built-in spotlights.\n", 63.8208628,20.3033002, CULTURAL, "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Norra_skenet%2C_av_Ernst_Nordin.JPG/800px-Norra_skenet%2C_av_Ernst_Nordin.JPG");
        Site buddha_i_grottan = new Site("8 11", "I en övergiven turbinsump där det för länge sedan producerades elektrisk energi speglar sig numera en mediterande konstnär i den svarta vattenytan. Konstverket är skapat av Fredrik Wretman.\n" +
                "Skenet från en strålkastare reflekteras i lätta krusningar som blir till ett vågspel mot salens sargade väggar bakom den orangeglödande figuren. Det fasta förflyktigas, botten tycks försvinna och beredskapen osäkras. Utifrån hörs de överblivna vattenmassorna från " +
                "kraftverksdammen en bit uppströms passera på väg mot havet.",63.8403323,20.1148213 , CULTURAL, "https://c2.staticflickr.com/2/1098/661267705_1ecc21e1d3_b.jpg");
        Site umedalens_skulpturpark = new Site("Umedalens skulpturpark", "From the opening in 1994 onwards, 190 eminent Swedish and international artists have exhibited their works in Umedalsparken. For 20 years Balticgruppen - the owner of the compound - has financed the art project" +
                " ”Umedalen Skulptur”, curated by Galleri Andersson/Sandström. Through the years Balticgruppen has purchased 44 sculptures forming today an impressive permanent collection. Umedalsparken is one of the county’s major tourist attractions with more than 20 000 visitors each year " +
                "and has received considerable attention and numerous awards. These include Swedish journals prize for Best Art Event in Sweden 2000, English Arts and Business Awards 2004, Council of Urban Planning price in 2005 for the best example of Art and Design in the public domain and Umeå Region Tourism Price 2007.\n" +
                "\n" +
                "Visiting Umedalens Skulpturpark is completely free and the park is open around the clock all year round.\n" +
                "Welcome!", 63.837744,20.1577667, CULTURAL, "http://www.umedalenskulptur.se/us/images/stories/2014/US_etta_web_2014.jpg");
        Site cube = new Site("2 x 3²", "Sculpture by Cristos Gianakos resebling two metal couches turned towards each other. Year: 1998.", 63.81909, 20.28118, CULTURAL, "https://upload.wikimedia.org/wikipedia/commons/thumb/1/13/2x3-Ume%C3%A5-2015-05-28.jpg/270px-2x3-Ume%C3%A5-2015-05-28.jpg");
        Site skin_4 = new Site("Skin 4", "Mehmet Ali Uysal creates large scale installations that are integrated into the material of the building they are situated in, transforming the venue and altering both the viewer's perception of space as well as how they move around it. Today, " +
                "the process of creating, exhibiting, and perceiving contemporary art is deeply intertwined with the austere, white walled gallery space that continuously erases traces of its history to look perfect and untouched. Uysal wants to invert this tradition and revive the gallery" +
                " space as a living entity by interfering directly with the structure of the white cube and deconstructing both its formal characteristics and inherited traditions.", 63.81967, 20.27878, CULTURAL,"http://www.umea.se/images/18.6f3b390214107b691a1e428/1380573374216/Skin+4+(2).jpg");
        Site väven = new Site("Väven", "I Väven, Umeås nya plattform för kultur och upplevelser, vävs människor och idéer samman – därav namnet. Genom att skapa förutsättningar för nya samarbeten mellan små och stora aktörer i Umeå hittar du här ett spektrum av upplevelser. I Väven kan du uppleva kultur i dess bredaste bemärkelse: från musik till mat, föreläsningar till förströelse, det spontana till det planerade, privat eller i affärer.\n"+
                "På Torget hittar du Stora Hotellet, hotellet U&Me, Gotthards krog, Kulturbageriet och DUÅ Delikatesser.", 63.8244777, 20.259454300000016, CULTURAL,"http://static1.squarespace.com/static/55738760e4b07ff6e5767faa/t/55f88c0be4b0c80742d398c1/1442352163898/" );
        Site saraLidmanTunneln = new Site("Sara Lidman-tunneln", "Uppförd 2013. Väggarna pryds av citat av västerbottniska Sara Lidman och här hörs fågelsång året om.", 63.82965, 20.265826, CULTURAL,"http://blogg.vk.se/wp-content/uploads/oldblog/1589/images/saratunneln8(1).JPG");

       // Religiuos
        Site hörnefors_nya_kyrka = new Site("Hörnefors nya kyrka", "Hörnefors nya kyrka bekostades av Mo och Domsjö AB. Enligt de första planerna skulle den uppföras i Norrbyn, högt och väl synlig från Norrbyskär och samtidigt på någorlunda bekvämt avstånd från Hörnefors. Nu blev det inte så. Den lilla men tunga stenkyrkan, projekterad för att stå på en klippa, kom att byggas på plan mark intill Hörneån i Hörnefors samhälle.\n" +
                "Exteriören med kraftfulla till mittskeppet adderade volymer i rött tegel med inslag av granit och korsvirke har utformats med engelsk sengotik som förebild. Tornhuven på det sidoställda sydvästtornet har karaktär av jugend.\n" +
                "Kyrkorummets snidade och målade ornamentik med stiliserade växtmotiv är tagna från bygdens barrskog och utförda i böljande jugendstil.", 63.620790, 19.912677, RELIGIOUS, "https://www.svenskakyrkan.se/bilder/rosornas-kyrka-v%C3%A5r.jpg");
        Site backensKyrka = new Site("Backens kyrka", "Mycket talar för att det funnits en kyrka redan på 1200- eller 1300-talet. Församlingen omnämns i en taxeringslängd från 1314 och i ett visitationsprotokoll från 1324. Vid restaureringen i början på 1950-talet påträffades under golvet ett stenparti som förmodligen är grunden till en tidigare kyrka.\n" +
                "Umeå landsförsamlings kyrka, eller Backens kyrka, är en av de största kyrkorna som uppfördes längs Norrlandskusten i början på 1500-talet när Jacob Ulfsson var ärkebiskop.\n" +
                "Ytterväggarna och gamla sakristian, till vänster om koret är från 1500-talet. Efter en brand 1893 slogs valven från 1500-talet ner i kyrkorummet. Nya valv med 1500-talets stjärnmönster som avbild uppfördes vid restaureringen på 1950-talet.\n" +
                "1986 förstördes kyrkan på nytt av en stor brand. Endast ytterväggarna och valven återstod. Då diskuterades om kyrkan skulle vårdas som ruin eller återuppbyggas. Efter beslut om återuppbyggnad bestämdes ”att så långt som möjligt ansluta till den ursprungliga kyrkobyggnadens arkitektoniska gestalt, arkitekturdetaljer och materialval utan att göra avkall på idag uttalade krav på allmän och liturgisk funktionsduglighet.”\n" +
                "Arkitekten Jerk Alton och konstnären Per Andersson anlitades. En bärande tanke var att kyrkorummet skulle gestalta hoppet om uppståndelse, skönhet och värme, det vill säga en försmak av paradiset redan här på jorden.",63.8380731,20.1563725, RELIGIOUS,"http://www.umea.se/images/18.73474df7141ec1b19d15615/1383648352953/Backens_kyrka_h.gif");
        Site umeåStadsKyrka = new Site("Umeå stadskyrka", "F O Lindströms nygotiska kyrka är resultatet av en bearbetning av ett förslag till kyrkobyggnad framtaget av Carl Möller, arkitekten bakom Johannes kyrka i Stockholm. Genom att införa sidoläktare lyckades Lindström minska kyrkans planyta och därmed sänka byggkostnaden. Kyrkan är en treskeppig hallkyrka med tvärskepp. Sittplatser fanns från början i hela mittskeppet och mittgång saknades. Tornet placerades, av hänsyn till svagheten i den branta älvbrinken sidoställt i nordväst.\n" +
                "Redan 1937 togs en mittgång upp. 1982 fick kyrkan en omdebatterad tillbyggnad i form av församlingslokaler mot söder, delvis nedgrävda i älvbrinken, och kyrkorummet fick en lillkyrka i koret och färgglad interiör.", 63.823552, 20.267803, RELIGIOUS,"https://c1.staticflickr.com/5/4046/4613453840_de7b53beeb.jpg");
        Site holmsundsKyrka = new Site("Holmsunds kyrka", "Här vid Umeälvens mynning anlade James Dickson & Co 1848 en lastageplats för vattensågen i Baggböle två mil uppströms. Ett samhälle med arbetarbostäder, herrgård, kyrka, prästgård och skola växte fram. \n" +
                "Kyrkan i nyklassicistisk stil utgör krönet på pastor J A Linders livsverk som arkitekt och byggare. Den nästan 80-årige pastorn har inte bara utfört alla byggnadsritningar till kyrkan och dess inredningsföremål. Han har även själv snidat alla dekorationer på predikstol, altare och läktarbröstning. Det vackra kyrkorummet är målat efter hans anvisningar. \n" +
                "De till kyrkan länkade församlingslokalerna är ljusa, luftiga och delvis nedsänkta i terrängen och underordnar sig kyrkan på ett fint sätt.",63.703577, 20.350006, RELIGIOUS,"http://www.umea.se/images/18.73474df7141ec1b19d14db7/1383570139256/Holmsunds-kyrka_h.gif");

        // Historical
        Site ringstrandska_villan = new Site("Ringstrandska villan", "När Mo och Domsjö AB:s nyanställde skogschef i Umeå, jägmästaren Nils G Ringstrand, 1897 behövde en representativ bostad på sin nya hemort vände han sig till arkitekt Kasper Salin, Stockholm. Denne hade ritat skogsbolagets arbetarbostäder på Norrbyskär (se: Norrbyskär!) i amerikansk spånbyggnadsstil som tilltalade Ringstrand.\n" +
                "Den villa som uppfördes efter ritningar av Gustaf Lindgren, kollega till Salin, har ett nationalromantiskt formspråk med fornnordiska inslag. Interiörens fria planlösning kommer till uttryck i en asymmetrisk fasadbehandling med takkupor, frontespiser och burspråk. Dekorativt formade foder och vindskivor, varierade fönstertyper, svarvade stolpar och ornament pryder fasaderna.", 63.823380, 20.272322, HISTORICAL, "http://www.umea.se/images/18.73474df7141ec1b19d14e8e/1383575780927/Ringstrandska_villan_h.gif");
        Site borgmästargården = new Site("Borgmästagården", "Med överblivet tegel från det nyss avslutade kyrkobygget på tomten intill uppförde kyrkobyggmästaren Per Eriksson 1892 detta lilla ”slott” för juristen och borgmästaren Albin Ahlstrand.\n" +
                "Trappgavlarna, hörntourellerna, fasadens mönstermurningar och blinderingar och det krenelerat trapphustornet med spetsig tornhuv är förmodligen utförda efter förebild från något mönsterblad – bygglovsritningen är osignerad!\n" +
                "Finurligt anpassat är Uno Nygrens kontorsbygge på gården. De ockra, rödrosa och blå färgtonerna från blinderingarnas målningar återkommer på nybyggets fönster, väggpaneler och takfot och får de båda husen att samspela.", 63.8234142,20.2656215, HISTORICAL, "http://www.umea.se/images/18.73474df7141ec1b19d14d84/1383568954431/Borgmastarvillan_h.gif");
        Site järnvägsstationen = new Site("Järnvägsstationen", "Bibanan Vännäs–Umeå invigdes 1896. I den nya stadsplanen gavs järnvägsstationen en framträdande plats i centrum av en monumental stjärnplats med strålgator åt flera håll. \n" +
                "Med hög mittkropp flankerad av lägre flyglar, frontespiser och branta tak ger stationshuset ett pampigt intryck, dock något förtaget av de höga husen runt torget. Fasadmaterialet är rött tegel med dekorativa inslag i form av mönstermurning med olikfärgat glaserat tegel. Murade takryttare ger stationen ett festligt uttryck.", 63.830063,20.2646253, HISTORICAL, "http://www.umea.se/images/18.2e9e2c2914ce7d186cf2357f/1432103708985/jvstn2014.jpg");
        Site scharinska_villan = new Site("Scharinska villan", "Sommaren 1890 praktiserade två unga arkitektstuderande, Ragnar Östberg och Carl Westman hos stadsarkitekten F O Lindström i Umeå. Kanske ledde detta till att Östberg senare fick uppdraget att rita en byggnad med kontor för den Scharinska trävarufirman och bostad för disponenten Egil Unander-Scharin och hans stora familj. \n" +
                "Det stora rosa putshuset vid Döbelns park med originell fasadkomposition, tätspröjsade vita fönster i murlivet och barockartade fasadpartier över fönsteraxlarna ägs sedan 1957 av Umeå kommun och har mellan 1957 och 2015 upplåtits till Umeå studentkårer genom olika avtal. Ragnar Östberg som längre fram i karriären blev känd som arkitekten bakom Stockholms stadshus, har ritat ytterligare två hus i Umeå.", 63.823616, 20.271358, HISTORICAL, "http://www.umea.se/images/18.73474df7141ec1b19d14e8f/1383575865418/Scharinska_villan_h.gif");
        Site tullkammaren = new Site ("Tullkammaren", "De många enskilda magasinen och bodarna som fanns i hamnen före branden 1888 kom inte att återuppbyggas. Istället bildade handelsmännen och staden ett bolag som uppförde ett gemensamt magasinshus med tullkammare. Byggnaden som var en del av stadens ansikte mot älven gavs en monumental prägel.\n" +
                "Den kraftiga byggnaden i mörkrosa tegel på grovhuggen sockel av granit var ursprungligen symmetrisk med tre spetsigt avslutade risaliter mot älven. Efter en brand 1908 omformades mittrisaliten till ett stabbigt torn och den västra risaliten togs bort – en anpassning till den teaterbyggnad som då fanns på granntomten i öster. ", 63.824934, 20.258398, HISTORICAL, "http://www.umea.se/images/18.2e9e2c2914ce7d186cf235b2/1432104091174/tullkamm2.jpg");
        Site stora_hotellet = new Site("Stora hotellet", "Vid Rådhusparken, väl synligt från älven som var tidens viktigaste kommunikationsled, lät Umeå sjömanshus uppföra en byggnad innehållande kombinerat sjömanshus och hotell. Här inrymdes även matsal, schweizeri, biljardsalong och festvåning med musikestrad. Fasaden i utpräglat festlig nyrenässansstil utmärks av symmetri och kontrastrik, okonventionell färgbehandling.\n" +
                "Byggnadens arkitekt var Viktor Åström. Han är inte så känd på riksplanet men har lämnat betydande spår efter sig i stadsbilden i Skellefteå, Umeå och framför allt Piteå. Åström var arkitekten bakom det numera rivna Åkerblomska huset i fornnordisk spånbyggnadsstil samt överste Helmer Huss ännu kvarvarande villa på Västra Norrlandsgatan.",63.8249943,20.2591963, HISTORICAL,"http://www.umea.se/images/18.73474df7141ec1b19d14e9d/1383576028690/Stora-hotellet_h.gif");
        Site moritzska_gården = new Site("Moritzska gården","Carl Fridolf Engelbert Sandgren, son till kakelugnsmakare i Umeå och verksam husritare i staden under perioden 1882-1896, översatte skickligt formspråket i stenstadens putsade nyrenässansfasader till norrländsk träarkitektur. \n" +
                "Till hans märkligaste arbeten hör Moritzska gården - en livfull och festlig träbyggnad med palatsliknande karaktär och drag av nyrenässans, barock och jugend. Den uppfördes som kontor och bostad för disponenten vid Sandviks ångsåg, Carl Gustaf Moritz. \n" +
                "Byggnaden flyttades 1984 från ett tidigare läge vid Rådhusesplanaden till sin nuvarande plats vid Döbelns park. I samband med flytten eldhärjades huset, varför halva byggnaden är en rekonstruktion.", 63.8239051,20.2690459, HISTORICAL,"http://www.umea.se/images/18.73474df7141ec1b19d14e22/1383571770295/Moritzka_garden_h.gif");
        Site länsresidenset = new Site("Länsresidenset", "Efter stadsbranden presenterades två alternnativ för det nya länsresidenset, ett med landshövdingebostad och ämbetsrum för landsstaten i skilda hus och ett där båda funktionerna rymdes i samma byggnad. Av ekonomiska skäl valdes det senare: en lång tvåvåningsbyggnad i italiensk nyrenässansstil med ämbetsrum i den putsade och rusticerade bottenvåningen och landshövdingens röda tegelfasad. Här ligger paradsviten mot söder med utsikt över residensparken och älven, medan trivialare utrymmen är placerade mot Storgatan i norr. \n" +
                "De fint anpassade flyglarna mot norr tillkom 1933 och har ritats av Dennis Sundberg." ,63.822852, 20.272868, HISTORICAL, "http://www.umea.se/images/18.73474df7141ec1b19d14e10/1383571039360/Lansresidens_h.gif");
        Site sävargården = new Site("Sävargården", "Sävargården uppfördes ursprungligen som herrgård vid bröderna Forsells sågverk i Sävar. Byggnaden - en pampig envåningsbyggnad med inredd vindsvåning under ett stort brutet avvalmat spåntak - skänktes 1927 till länets nybildade hembygdsförening.\n" +
                "I nedmonterat skick kördes den med häst och släde till Gammlia, där den återuppfördes under ledning av länsarkitekt Edvard Lundquist. Exteriören bibehölls, interiören däremot utformades fritt i nyklassicistisk anda, så även de nya brokvistarna med dubbla trapplopp. På sin nya plats har byggnaden, som idag ägs av Umeå universitet, använts för café och restaurang. Den tidstypiska tillbyggnaden från 1963 rymmer restaurangkök och galleri för konsthögskolan.", 63.828462,20.290932, HISTORICAL,"http://www.umea.se/images/18.73474df7141ec1b19d14e9f/1383576061560/S%C3%A4varg%C3%A5rden_h.gif" );
        Site olofsforsBruk = new Site("Olofsfors bruk", "Olofsfors Bruksmuseum - en del av Sveriges mest välbevarande järnbruk - Olofsfors bruk.\n" +
                "Olofsfors bruksmuseum sköts av en stiftelse och har museum med både fasta och tillfälliga utställningar.\n" +
                "\n" +
                "Den fasta utställningen finns i den gamla kuskbostaden bredvid informationsdisken i Ladan. I denna utställning har du möjlighet att se och lära dig mer om hur människorna på bruket levde - från barn till vuxna, från arbetare till disponent. Tillfälliga utställningar brukar exempelvis hållas i den så kallade Ladan, mitt på bruket.\n" +
                "\n" +
                "Olofsfors bruk är idag ett av Sveriges bäst bevarande järnbruk och ett av Västerbottens största besöksmål. Brukets historia berättas på olika sätt; genom utställningar, guidade visningar och aktiviteter.", 63.58058699999999, 19.441219000000046, HISTORICAL,"http://olofsforsbruk.nu/wp-content/uploads/2012/06/masugn_web-604x320.jpg");
        Site cellFängelset = new Site("Cellfängelset", "Cellfängelset är det äldsta stenhuset i Umeå och ett av de få hus som överlevde branden 1888. Det uppfördes efter typritningar utarbetade med fängelser i Amerika och Tyskland som förebild. Jämfört med tidigare förhållanden, då fångarna förvarades fastkedjade i källarvalv i slott och borgar, innebar det nya systemtet en stor förbättring. \n" +
                "Byggnaden har placerats med kortsidan som huvudfasad mot gatan i norr. Fångavdelningen åt söder omfattade 24 celler fördelade i två våningar på var sida om ett öppet galleri. Mot gatan finns ett vaktrum, kök och bostad för fängelsedirektören. Byggnaden - med slätputsade fasader, medeltidsinspirerade former och omgivande dubbelplank - är ett av de bäst bevarade cellfängelserna i landet.", 63.822337, 20.275396, HISTORICAL,"https://exp.cdn-hotels.com/hotels/8000000/7830000/7827400/7827340/7827340_13_y.jpg");
        Site baggböleHerrgård = new Site("Baggböle herrgård", "Om storhetstiden för sågverket i Baggböle - och för den olovliga avverkningen av kronoskog under perioden 1860 - 1880 - minner förvaltarbostaden, en byggnad i empirestil, uppe på älvbrinken med utsikt över anläggningen vid forsen. Herrgården var på sin tid en imponerande byggnad: av timmer och klädd med slätpanel, större och bredare än bondgårdarna i trakten och målad med ljus oljefärg, så att den på avstånd liknade ett stenhus. Fasaden med mittfronton och pilastrar är ett av de finaste exemplen på nyklassiscism i umetrakten. \n" +
                "Huset ritades av pastor Johan Anders Linder i Umeå, en mångkunning person som ofta anlitades för att försköna fasaderna på borgarnas hus under 1800-talets förra hälft.  ", 63.84032999999999, 20.117009999999937, HISTORICAL,"http://www.umea.se/images/18.73474df7141ec1b19d14d79/1383568853404/Baggb%C3%B6le-Herrg%C3%A5rd_h.jpg");
        Site rådhuset = new Site("Rådhuset", "För att leda återuppbyggnaden av staden efter branden anställdes som stadsarkitekt Fredrik Olaus Lindström, Stockholm. Förutom att upprätta förslag till stadsplan och granska ritningar ingick i hans uppdrag att rita stadens offentliga byggnader. \n" +
                "Rådhuset med huvudfasad mot älven kom att inrymma lokaler för stadens administration, rättssal, post, telegraf och polisstation. För byggnaden valde Lindström holländsk nyrenässans, en stil som bättre än den strikta italienska nyrenässansen motsvarade 80-talsrealisternas strävan efter äkthet i material och konstruktion. Med rött fasadtegel, asymmetrisk fasad, hörntorn, trappgavlar och livlig taksiluett har byggnaden getts ett uttryck som är både värdigt och festligt.", 63.825337, 20.262847, HISTORICAL,"http://www.umea.se/images/18.53d383fe142675f74e6c61/1384780829004/Radhuset_liten_h.gif");
        Site norrbyskär = new Site("Norrbyskär", "På kala och tidigare obebodda skär utanför Norrbyn anlades vid 1900-talets början ett sågverkssamhälle. På kort tid byggde Mo och Domsjö AB här upp en stor anläggning med såghus, maskinhall, fraktbanor, timmerupplag och kajer. Under ledning av företagets socialt intresserade vd, Frans Kempe, planerades ett möstersamhälle för sågverkets anställda. Kasper Salins arkitektkontor i Stockholm anlitades för att rita husen. \n" +
                "På Långgrundet uppfördes i en lång rad av timrade panelklädda bostadshus i trä, ljusmålade med pappklädda branta tak, frontespiser med sadeltak och stora taksprång. - en byggnadsstil med förebilder i amerikansk villaariktektur. Varje hus rymde fyra lägenheter. Skolan med kyrksal bildar en liten plats i fonden av den kilometerlånga gatan. \n" +
                "I en andra etapp uppfördes på Stuguskär ytterligare en grupp bostadshus, denna gång i rött tegel men med liknande formspråk och planlösning. Brokvistar och gavelspetsar pryds här liksom på Långgrundet av lövsågade dekorationer i säregen stil. De rödfärgade uthusen, stora vedbodar och små avträden, har alla symmetrisk komposition och tunna papptak med stora språng. \n" +
                "Sågverket lades ner 1952. Av anläggningen finns idag bara maskinhallen kvar. Den rymmer sedan flera år tillbaka ett museum som berättar om skärens historia. Bostadshusen har sålts till privatpersoner och används idag som fritidshus. Sågverkssamhället har förvandlats till semesteridyll.", 63.55271999999999, 19.876070000000027, HISTORICAL,"http://www.umea.se/images/18.73474df7141ec1b19d14e24/1383571793773/Norrbyskar_1_h.jpg");


        mySites.allSites.add(ringstrandska_villan);
        mySites.allSites.add(borgmästargården);
        mySites.allSites.add(sparken);
        mySites.allSites.add(järnvägsstationen);
        mySites.allSites.add(hörnefors_nya_kyrka);
        mySites.allSites.add(scharinska_villan);
        mySites.allSites.add(norra_skenet);
        mySites.allSites.add(tullkammaren);
        mySites.allSites.add(buddha_i_grottan);
        mySites.allSites.add(umedalens_skulpturpark);
        mySites.allSites.add(cube);
        mySites.allSites.add(skin_4);
        mySites.allSites.add(stora_hotellet);
        mySites.allSites.add(moritzska_gården);
        mySites.allSites.add(länsresidenset);
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

*/

        mySites.allSites.add(bodaBorg);
        mySites.allSites.add(bonnstan);
        mySites.allSites.add(fyren);

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



