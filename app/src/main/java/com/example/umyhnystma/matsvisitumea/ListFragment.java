package com.example.umyhnystma.matsvisitumea;

import android.content.Context;
import android.net.Uri;
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
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    ListView    containerListViewSite;
    ArrayList<String> itemsInSiteList = new ArrayList<>();
    MyArrayAdapter myArrayAdapter;


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

            textView.setText(itemsInSiteList.get(position));


            return blowupmenu;                                                      // Här returnerars menuitemblowup.xmls root-layout
        }
    }

    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  view = inflater.inflate(R.layout.fragment_list, container, false);
        view = inflater.inflate(R.layout.fragment_sites, container, false);

        for (int i = 0; i < ((InfoDetailActivity)getActivity()).mySites.size(); i ++){
            itemsInSiteList.add(((InfoDetailActivity)getActivity()).mySites.get(i).getName());
        }

        //itemsInSiteList.add("Backens kyrka");
        //itemsInSiteList.add("Sävargården");
        // itemsInSiteList.add("Baggböle Herrgård");
        //itemsInSiteList.add("Umeå Stadskyrka");
        //itemsInSiteList.add("Gammlia");
        //itemsInSiteList.add("Smörasken");

        containerListViewSite =(ListView)view.findViewById(R.id.containerListViewSite);

        myArrayAdapter = new MyArrayAdapter(getActivity(),R.layout.fragment_in_sitelist_blow_up_second, itemsInSiteList);



        containerListViewSite.setAdapter(myArrayAdapter); //adapter skickas till min ListView som kallas containerListViewSite och ligger i fragment_sites.xml

        containerListViewSite.setOnItemClickListener(new AdapterView.OnItemClickListener() {         // Tar hand om klick i list-vyn
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {


                // fragmentMap  mMap

                //   MapFragment tmp = (MapFragment)((MainActivity)getActivity()).fragmentMap;

            //    ((MainActivity)getActivity()).finish(); // Ska döda mainActivity

                String type = itemsInSiteList.get(pos);
               Log.i("MIN_TAG", "typ: " + type);

               // Intent intent = new Intent(getActivity(), InfoDetailActivity.class);     // Anropar under runtime class-filen
                //intent.putExtra(INTENT_NOTE_STRING, currentNote.note);                 // Sträng skickas med bundle
                //intent.putExtra(INTENT_POSITON_NUMBER, pos);                           // Position skickas med bundle

             //   intent.putExtra("KEY", 2);

             //   getActivity().startActivity(intent);

            }
        });



        //////////////////////////////////////////////////

        // Inflate the layout for this fragment
        return view;

    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
