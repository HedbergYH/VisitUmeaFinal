package com.example.umyhnystma.matsvisitumea;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationMessage extends Fragment {

    ImageView imageView, cancelMessage;
    TextView titleText, descriptionText;
    FragmentManager fm;
    FragmentTransaction trans;

    Site mySite;

    public LocationMessage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentLocationMessage
        View view = inflater.inflate(R.layout.fragment_location_message, container, false);

        mySite = ((InfoDetailActivity)getActivity()).siteShortInfoMessage;

        cancelMessage = (ImageView)view.findViewById(R.id.cancelMessage);
        imageView = (ImageView)view.findViewById(R.id.visitLogoImage);
        titleText = (TextView)view.findViewById(R.id.titleTextView);
        descriptionText = (TextView)view.findViewById(R.id.descriptionTextView);

        descriptionText.setText(mySite.description);
        titleText.setText(mySite.name);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm = getActivity().getSupportFragmentManager();
                trans = fm.beginTransaction();
                Fragment detailInfoFragment = new DetailInfoFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("KEY_SERIALIZABLE", mySite);
                detailInfoFragment.setArguments(bundle);

                trans.hide(((InfoDetailActivity) getActivity()).fragmentMap).commit();
                ((InfoDetailActivity) getActivity()).hideTabBars();

                trans = fm.beginTransaction();
                trans.add(R.id.activity_info_detail_relroot_container, detailInfoFragment, "DETAIL_INFO_FRAGMENT_FROM_MAP").addToBackStack("MY_TAG").commit();
                //Byt ut hela fragmentet (InfoMapFragment) till DetailInfo-Fragment och skicka med ett objekt eller en referens till ett objekt


            }
        });

        cancelMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        return view;
    }

}
