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

    ToggleChange myToggleChange;
    ToggleButton toggleGPS, toggleLocationGuide;

    public ToggleButtonsMainActivity() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        //Interface mellan MapFragment och ToggleButtons via MainActivity
        super.onAttach(context);
        myToggleChange = (ToggleChange)getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_toggle_buttons_main, container, false);

        toggleGPS = (ToggleButton)v.findViewById(R.id.toggleGPS);

        toggleGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if(isChecked){

                    Log.i("MIN_TAG", "if-case--> isChecked is:" + isChecked);

                    //isChecked = false;

                    myToggleChange.onChangedGPS(1, isChecked);

                }else{

                    Log.i("MIN_TAG", "isChecked is: " + isChecked);

                    //isChecked = true;

                    myToggleChange.onChangedGPS(2, isChecked);
                }



            }
        });


        return v;
    }

}
