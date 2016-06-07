package com.example.umyhnystma.matsvisitumea;



import android.content.Context;
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
 * A simple {@link Fragment} subclass.
 */
public class DetailInfoFragment extends Fragment {

    FragmentManager fm;
    FragmentTransaction trans;
    Fragment fragmentMap,fragmentTrackMap;

    RelativeLayout mainContainerInFragmentDetailInfo,mapOrListContainer;

    View view;
    ImageView imageview;
    TextView smalltext_in_fragment_detail_info,text_in_fragment_detail_info, title_in_fragment_detail_info;
    Button trackInFragmentDetailInfo,backInFragmentDetailInfo;

    OnClick myClicker;


    public DetailInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {  // Mats: kan tas bort
        super.onAttach(context);
//        myClicker = (OnClick) context;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


     //   fragmentMap = ((InfoDetailActivity)getActivity()).fragmentMap;
          //  ((InfoDetailActivity)getActivity()).invokeMapFragment();
          //  getActivity().getSupportFragmentManager().beginTransaction().show(map).commit();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_info_rev_b, container, false);
        // Inflate the layout for this fragmentLocationMessage

        smalltext_in_fragment_detail_info = (TextView) view.findViewById(R.id.smalltext_in_fragment_detail_info);

        title_in_fragment_detail_info = (TextView)view.findViewById(R.id.title_in_fragment_detail_info);

        backInFragmentDetailInfo = (Button) view.findViewById(R.id.backInFragmentDetailInfo);
        trackInFragmentDetailInfo = (Button) view.findViewById(R.id.trackInFragmentDetailInfo);


        setHasOptionsMenu(true);


;


/*
        Bundle bundle = getArguments();
        int position = bundle.getInt("KEY");

        String info = ((InfoDetailActivity)getActivity()).mySites.get(position).getName();

*/
        Bundle bundle2 = getArguments();
        Site mySite = (Site)bundle2.getSerializable("KEY_SERIALIZABLE");
        String objectTitle = mySite.getName();


        Log.i("TAG", "I FRAGMENTET. Borde synas");

        title_in_fragment_detail_info.setText(objectTitle);
        smalltext_in_fragment_detail_info.setText(mySite.getDescription());


         // Skall anropas separat för vaje bild
        String myPicture = mySite.getPictureURL();

        Log.i("TAG","myPicture = " + myPicture);

        ImageLoader myImageLoader = ImageLoader.getInstance();
        myImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageview = (ImageView)view.findViewById(R.id.pictureofbuilding);
        myImageLoader.displayImage(myPicture,imageview);


        backInFragmentDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MIN_TAG", "backInFragmentDetailInfo i DetailInfoFragment");
                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                Log.i("MIN_TAG", "backInFragmentDetailInfo-count är: "+ count);
                if (count == 0) {
                    getActivity().finish();
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });


        trackInFragmentDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MIN_TAG", "TRACK är klickat: ");

              //  myClicker.onClick(100); //metoden onClick anropas i kombinerade aktivitets- och onClick-objektet // Mats: kan tas bort
                fm = getActivity().getSupportFragmentManager();
                trans = fm.beginTransaction();
                MapTrackFragment mapTrackFragment = new MapTrackFragment();
                trans.add(R.id.activity_info_detail_relroot_container, mapTrackFragment); // funkar
                trans.addToBackStack("TAG");
                trans.commit();
            }
        });


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

            //Starta trackfragmentet här
            Toast.makeText(getActivity(), "Clicked on track fragment start.", Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
