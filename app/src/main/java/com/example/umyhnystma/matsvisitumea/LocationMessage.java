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

    Site myTestsite;

    public LocationMessage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_message, container, false);

        myTestsite = new Site();

        myTestsite.setName("Backens jävla kyrka");
        myTestsite.setDescription("Backens hela historia och allt som kan vara intressant om Backens jävla kyrka. Fan.");

        cancelMessage = (ImageView)view.findViewById(R.id.cancelMessage);
        imageView = (ImageView)view.findViewById(R.id.visitLogoImage);
        titleText = (TextView)view.findViewById(R.id.titleTextView);
        descriptionText = (TextView)view.findViewById(R.id.descriptionTextView);

        descriptionText.setText(myTestsite.description);
        titleText.setText(myTestsite.name);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trans = fm.beginTransaction();
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
