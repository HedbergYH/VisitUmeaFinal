package com.example.umyhnystma.matsvisitumea;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import companydomain.visitumea.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }
        // Inflate the view for the fragmentMap based on layout XML
        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_search, container, false);




        /*TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        tvLabel.setText(page + " -- " + title);*/


            return view;
        }
}

