package com.example.umyhnystma.matsvisitumea;

import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragmentLocationMessage must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragmentLocationMessage.
 */
public class ListFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragmentLocationMessage initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    FragmentManager fm;
    FragmentTransaction trans;
    Fragment detailInfoFragment;


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
            View blowupmenu = inflater.inflate(R.layout.fragment_in_sitelist_blow_up_second, containerListViewSite, false);
            TextView textView = (TextView) blowupmenu.findViewById(R.id.siteListText); // siteListText ligger i fragment_in_sitelist_blow_up.xml
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
     * this fragmentLocationMessage using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragmentLocationMessage ListFragment.
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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sites, container, false);

        for (int i = 0; i < ((InfoDetailActivity)getActivity()).mySites.size(); i ++){
            itemsInSiteList.add(((InfoDetailActivity)getActivity()).mySites.get(i).getName());
        }

        containerListViewSite =(ListView)view.findViewById(R.id.containerListViewSite);

        myArrayAdapter = new MyArrayAdapter(getActivity(),R.layout.fragment_in_sitelist_blow_up_second, itemsInSiteList);

        containerListViewSite.setAdapter(myArrayAdapter); //adapter skickas till min ListView som kallas containerListViewSite och ligger i fragment_sites.xml

        containerListViewSite.setOnItemClickListener(new AdapterView.OnItemClickListener() {         // Tar hand om klick i list-vyn
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("KEY_SERIALIZABLE", (((InfoDetailActivity) getActivity()).mySites.get(pos)));

        fm = getActivity().getSupportFragmentManager();
        trans = fm.beginTransaction();
        detailInfoFragment = new DetailInfoFragment();

        detailInfoFragment.setArguments(bundle);

        trans = fm.beginTransaction();
        trans.hide(((InfoDetailActivity) getActivity()).listFragment).commit();

        ((InfoDetailActivity) getActivity()).hideTabBars();

        trans = fm.beginTransaction();
        trans.add(R.id.activity_info_detail_relroot_container, detailInfoFragment, "DETAIL_INFO_FRAGMENT_FROM_LIST").addToBackStack("MY_TAG").commit(); // tar fram knappfragmentet

        String type = itemsInSiteList.get(pos);
            }
        });
        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
