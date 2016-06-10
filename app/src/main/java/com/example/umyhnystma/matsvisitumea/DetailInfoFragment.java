package com.example.umyhnystma.matsvisitumea;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.Serializable;

/**
 * Fragmentet visar bild och text för vald byggnad
 */
public class DetailInfoFragment extends Fragment {
//
    View view;
    ImageView imageview;
    TextView smalltext_in_fragment_detail_info, title_in_fragment_detail_info;
    Site mySelectedSite;


    public DetailInfoFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_info_rev_b, container, false);
        // Inflate the layout for this fragmentLocationMessage

        smalltext_in_fragment_detail_info = (TextView) view.findViewById(R.id.smalltext_in_fragment_detail_info);
        title_in_fragment_detail_info = (TextView)view.findViewById(R.id.title_in_fragment_detail_info);

        setHasOptionsMenu(true);

        Bundle bundle2 = getArguments();
        mySelectedSite = (Site)bundle2.getSerializable("KEY_SERIALIZABLE");
        String objectTitle = mySelectedSite.getName();

        title_in_fragment_detail_info.setText(objectTitle);
        smalltext_in_fragment_detail_info.setText(mySelectedSite.getDescription());

         // Skall anropas separat för vaje bild
        String myPicture = mySelectedSite.getPictureURL();

        ImageLoader myImageLoader = ImageLoader.getInstance();
        myImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageview = (ImageView)view.findViewById(R.id.pictureofbuilding);
        myImageLoader.displayImage(myPicture,imageview);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_detailinfo, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.track){

            double targetLatitude = mySelectedSite.latitude;
            double targetLongitude = mySelectedSite.longitude;

            double startLatitude = ((InfoDetailActivity)getActivity()).mCurrentLocation.getLatitude();
            double startLongitude = ((InfoDetailActivity)getActivity()).mCurrentLocation.getLongitude();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+startLatitude+","+startLongitude+"&daddr="+targetLatitude+","+targetLongitude));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
