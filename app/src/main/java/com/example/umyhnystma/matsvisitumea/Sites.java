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


/**
 * A simple {@link Fragment} subclass.
 */
public class Sites extends Fragment {


     ListView    containerListViewSite;
     ArrayList<String> itemsInCategoryList = new ArrayList<>();
     MyArrayAdapter myArrayAdapter;

    private class MyArrayAdapter extends ArrayAdapter<String> { // Custom Arrayadapter, borde brytas ut
        public MyArrayAdapter(Context context, int resource, ArrayList<String> itemsInCategoryList) {
            super(context, resource, itemsInCategoryList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getActivity().getLayoutInflater();

            //Bestämmer vad som ska blåsas upp och vart
            View blowupmenu = inflater.inflate(R.layout.fragment_in_sitelist_blow_up, containerListViewSite, false);
            TextView textView = (TextView) blowupmenu.findViewById(R.id.siteListText); // siteListText ligger i fragment_in_sitelist_blow_up.xml

            textView.setText(itemsInCategoryList.get(position));

            return blowupmenu;                                                      // Här returnerars menuitemblowup.xmls root-layout
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sites, container, false);

        itemsInCategoryList.add("All");
        itemsInCategoryList.add("Cultural");
        itemsInCategoryList.add("Religious");
        itemsInCategoryList.add("Historical");

        containerListViewSite =(ListView)view.findViewById(R.id.containerListViewSite);
        myArrayAdapter = new MyArrayAdapter(getActivity(),R.layout.fragment_in_sitelist_blow_up, itemsInCategoryList);
        containerListViewSite.setAdapter(myArrayAdapter); //adapter skickas till min ListView som kallas containerListViewSite och ligger i fragment_sites.xml

        containerListViewSite.setOnItemClickListener(new AdapterView.OnItemClickListener() {         // Tar hand om klick i list-vyn
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {


                String type = itemsInCategoryList.get(pos);
                Log.i("MIN_TAG", "typ: " + type);

                Intent intent = new Intent(getActivity(), InfoDetailActivity.class);     // Anropar under runtime class-filen

                if(pos == 0){
                    intent.putExtra("MySites", ((MainActivity) getActivity()).mySites.allSites);
                }else if(pos == 1){
                    intent.putExtra("MySites", ((MainActivity) getActivity()).mySites.culturalSites);
                }else if(pos == 2){
                    intent.putExtra("MySites", ((MainActivity) getActivity()).mySites.religiousSites);
                }else{
                    intent.putExtra("MySites", ((MainActivity) getActivity()).mySites.historicalSites);
                }
                getActivity().startActivity(intent);
            }
        });

        return view;
    }




    @Override
    public void onResume(){
        super.onResume();

        Log.i("MIN_TAG","onResume i SitesFragment");

    }
    @Override
    public void onPause() {
        super.onPause();

        Log.i("MIN_TAG","onPause i SitesFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MIN_TAG", "onDestroy i SitesFragment");
    }

}
