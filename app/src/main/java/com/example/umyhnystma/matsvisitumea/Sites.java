package com.example.umyhnystma.matsvisitumea;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;


import java.util.ArrayList;




/**
 * A simple {@link Fragment} subclass.
 */
public class Sites extends Fragment {


     ListView    containerListViewSite;
     ArrayList<String> itemsInSiteList = new ArrayList<>();
     MyArrayAdapter myArrayAdapter;

    ///////////////////////////////////////////////////////////////////

    private class MyArrayAdapter extends ArrayAdapter<String> { // Custom Arrayadapter, borde brytas ut
        public MyArrayAdapter(Context context, int resource, ArrayList<String> itemsInSiteList) {
            super(context, resource, itemsInSiteList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getActivity().getLayoutInflater();

           // View blowupmenu = inflater.inflate(R.layout.dance_list_item, listView, false); //Bestämmer vad som ska blåsas upp och vart
            View blowupmenu = inflater.inflate(R.layout.fragment_in_sitelist_blow_up, containerListViewSite, false);
/*
            TextView danceCourseTitle = (TextView)blowupmenu.findViewById(R.id.titleTextView);  // Namnet på danskursen
            TextView teacherTextView = (TextView)blowupmenu.findViewById(R.id.teacherTextView);
            TextView danceStyleTextView = (TextView)blowupmenu.findViewById(R.id.danceStyleTextView);
            TextView statusTextView = (TextView)blowupmenu.findViewById(R.id.statusTextView);
            danceCourseTitle.setText(courses.get(position).getTitle());

            teacherTextView.setText(courses.get(position).getTeacher());
            danceStyleTextView.setText(courses.get(position).getDanceStyle());
            statusTextView.setText(courses.get(position).getStatus());*/
             TextView textView = (TextView) blowupmenu.findViewById(R.id.siteListText); // siteListText ligger i fragment_in_sitelist_blow_up.xml
           //  textView.setText("Testar");
            textView.setText(itemsInSiteList.get(position));

            return blowupmenu;                                                      // Här returnerars menuitemblowup.xmls root-layout
        }
    }





/////////////////////////////////////////////////////////////////////
    // Här ska listfragmentet
    // Inflate the view for the fragment based on layout XML

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_sites, container, false);

        itemsInSiteList.add("All");
        itemsInSiteList.add("Culture");
        itemsInSiteList.add("Religious");
        itemsInSiteList.add("Historical");

        containerListViewSite =(ListView)view.findViewById(R.id.containerListViewSite);
        myArrayAdapter = new MyArrayAdapter(getActivity(),R.layout.fragment_in_sitelist_blow_up, itemsInSiteList);

        containerListViewSite.setAdapter(myArrayAdapter); //adapter skickas till min ListView som kallas containerListViewSite och ligger i fragment_fragment_sites.xml


        return view;
    }
}
