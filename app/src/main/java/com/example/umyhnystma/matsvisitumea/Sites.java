package com.example.umyhnystma.matsvisitumea;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

            //Bestämmer vad som ska blåsas upp och vart
            View blowupmenu = inflater.inflate(R.layout.fragment_in_sitelist_blow_up, containerListViewSite, false);
            TextView textView = (TextView) blowupmenu.findViewById(R.id.siteListText); // siteListText ligger i fragment_in_sitelist_blow_up.xml

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
        View view = inflater.inflate(R.layout.fragment_sites, container, false);

        itemsInSiteList.add("All");
        itemsInSiteList.add("Culture");
        itemsInSiteList.add("Religious");
        itemsInSiteList.add("Historical");

        containerListViewSite =(ListView)view.findViewById(R.id.containerListViewSite);
        myArrayAdapter = new MyArrayAdapter(getActivity(),R.layout.fragment_in_sitelist_blow_up, itemsInSiteList);
        containerListViewSite.setAdapter(myArrayAdapter); //adapter skickas till min ListView som kallas containerListViewSite och ligger i fragment_sites.xml

        containerListViewSite.setOnItemClickListener(new AdapterView.OnItemClickListener() {         // Tar hand om klick i list-vyn
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                String type = itemsInSiteList.get(pos);
                Log.i("MIN_TAG", "typ: " + type);

                Intent intent = new Intent(getActivity(), InfoDetailActivity.class);     // Anropar under runtime class-filen
                //intent.putExtra(INTENT_NOTE_STRING, currentNote.note);                 // Sträng skickas med bundle
                //intent.putExtra(INTENT_POSITON_NUMBER, pos);                           // Position skickas med bundle
                startActivity(intent);

            }
        });
        return view;
    }
}
