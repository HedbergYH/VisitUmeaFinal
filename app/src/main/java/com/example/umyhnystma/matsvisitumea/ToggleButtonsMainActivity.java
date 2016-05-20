package com.example.umyhnystma.matsvisitumea;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToggleButtonsMainActivity extends Fragment {

    ToggleButton toggleLocationGuide;

    public ToggleButtonsMainActivity() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_toggle_buttons_main, container, false);

        toggleLocationGuide = (ToggleButton)v.findViewById(R.id.toggleLocationGuide);

        toggleLocationGuide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    Log.i("MIN_TAG", "if-case--> isChecked is:" + isChecked);

                    //isChecked = false; Stäng av kod för location guide

                }else{

                    Log.i("MIN_TAG", "isChecked is: " + isChecked);

                    //isChecked = true; Gör kod för location guide

                }

            }
        });


        return v;
    }

}
