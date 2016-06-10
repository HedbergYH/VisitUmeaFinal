package com.example.umyhnystma.matsvisitumea;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//import companydomain.visitumea.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {

    MyArrayAdapter myAdapter;
    ArrayList <Site> mySearchedItems = new ArrayList<>();
    ListView mListView;
    SearchView mSearchView;
    TextView textView;


    public class MyArrayAdapter extends ArrayAdapter<Site>{
        public MyArrayAdapter(Context context, int resource, ArrayList<Site> searchedItems){
            super(context, resource, searchedItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.fragment_in_sitelist_blow_up_second, mListView, false);

            textView = (TextView)v.findViewById(R.id.siteListText);

            textView.setText(mySearchedItems.get(position).getName());

            return v;
        }
    }


        // Inflate the view for the fragmentMap based on layout XML
        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_search, container, false);

            mSearchView = (SearchView)view.findViewById(R.id.localSearch);
            myAdapter = new MyArrayAdapter(getActivity(),R.layout.fragment_in_sitelist_blow_up_second, mySearchedItems);
            mListView = (ListView)view.findViewById(R.id.listView);
            mListView.setAdapter(myAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ((MainActivity)getActivity()).showInfoDetailFragmentForSite(mySearchedItems.get(position));

                }
            });

            mSearchView.setQueryHint("Search");

            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    Log.i("TAG","onQueryTextSubmit");
                    mySearchedItems.clear();

                    searchItems(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

                public void searchItems(String query){

                    Log.i("TAG","searching items");
                    Log.i("TAG","query = " + query);

                    for(int i = 0; i < ((MainActivity)getActivity()).mySites.allSites.size(); i++){

                        if((((MainActivity) getActivity()).mySites.allSites.get(i).getName().toLowerCase()).contains(query.toLowerCase())){

                            mySearchedItems.add(((MainActivity)getActivity()).mySites.allSites.get(i));
                        }

                    }

                    Log.i("TAG","SearchedItemsSize = "+mySearchedItems.size());

                    myAdapter.notifyDataSetChanged();

                }
            });


            return view;
        }
}

