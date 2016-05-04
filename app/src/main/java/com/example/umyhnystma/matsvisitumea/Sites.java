package com.example.umyhnystma.matsvisitumea;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class Sites extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    ArrayList<Site> siteList;

    // newInstance constructor for creating fragmentMap with arguments
    public static Sites newInstance(int page, String title) {
        Sites fragmentFirst = new Sites();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

        siteList = new ArrayList<>(); // List of sites to connect with adapter


    }

    // Inflate the view for the fragmentMap based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_sites, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        tvLabel.setText(page + " -- " + title);
        return view;
    }
}
