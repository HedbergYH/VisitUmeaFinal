package com.example.umyhnystma.matsvisitumea;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailInfoFragment extends Fragment {

    View view;
    ImageView imageview;
    TextView smalltext_in_fragment_detail_info,text_in_fragment_detail_info, title_in_fragment_detail_info;
    Button trackInFragmentDetailInfo,backInFragmentDetailInfo;

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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_info_rev_b, container, false);

        smalltext_in_fragment_detail_info = (TextView) view.findViewById(R.id.smalltext_in_fragment_detail_info);
        title_in_fragment_detail_info = (TextView) view.findViewById(R.id.title_in_fragment_detail_info);

        backInFragmentDetailInfo = (Button) view.findViewById(R.id.backInFragmentDetailInfo);
        trackInFragmentDetailInfo = (Button) view.findViewById(R.id.trackInFragmentDetailInfo);


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
                createTrackMapFragment();
            }
        });


        return view;
    }


    public void  createTrackMapFragment(){


        Log.i("MIN_TAG", "createTrackMapFragment() är anropat: ");


    }



}
