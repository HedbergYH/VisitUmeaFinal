package com.example.umyhnystma.matsvisitumea;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragmentLocationMessage must implement the
 * {@link ButtonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ButtonFragment#newInstance} factory method to
 * create an instance of this fragmentLocationMessage.
 */
public class ButtonFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragmentLocationMessage initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View v;
    private  ImageView mapImage,listImage;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Fragment list;
    Fragment map;

    private OnFragmentInteractionListener mListener;

    public ButtonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragmentLocationMessage using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragmentLocationMessage ButtonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ButtonFragment newInstance(String param1, String param2) {
        ButtonFragment fragment = new ButtonFragment();
        Bundle args = new Bundle();;             //Används ej
        args.putString(ARG_PARAM1, param1);     //Används ej
        args.putString(ARG_PARAM2, param2); ;   //Används ej
        fragment.setArguments(args);;           //Används ej
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        map = ((InfoDetailActivity)getActivity()).fragmentMap;
        list = ((InfoDetailActivity)getActivity()).listFragment;

        ((InfoDetailActivity)getActivity()).invokeListFragment();

        getActivity().getSupportFragmentManager().beginTransaction().hide(map).commit();




        if (getArguments() != null) {//Används ej
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_button, container, false);
        // Inflate the layout for this fragmentLocationMessage

        mapImage = (ImageView) v.findViewById(R.id.buttonmapwithtext);
        listImage = (ImageView) v.findViewById(R.id.buttonlistwithtext);

        mapImage.setColorFilter(Color.argb(120, 0, 0, 0));                  // För att kart-knapp ska vara nergråad vid start

        mapImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("TAG", "Clicked map");

                mapImage.setColorFilter(Color.argb(0, 0, 0, 0));
                listImage.setColorFilter(Color.argb(120, 0, 0, 0));
                //((InfoDetailActivity)getActivity()).invokeMapFragment();

                if(list.isVisible()){
                    getActivity().getSupportFragmentManager().beginTransaction().hide(list).commit();

                }

                getActivity().getSupportFragmentManager().beginTransaction().show(map).commit();
                return false;
            }
        });

        listImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("TAG", "Clicked list");

                if(map.isVisible()){

                    getActivity().getSupportFragmentManager().beginTransaction().hide(map).commit();

                }

                listImage.setColorFilter(Color.argb(0, 0, 0, 0));
                mapImage.setColorFilter(Color.argb(120, 0, 0, 0));
                //((InfoDetailActivity)getActivity()).invokeListFragment();

                getActivity().getSupportFragmentManager().beginTransaction().show(list).commit();
                return false;
            }
        });

        return v;
    }


    // TODO: Rename method, update argument and hook method into UI event


    public void onButtonPressed(Uri uri) {//Används ej?
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * fragmentLocationMessage to allow an interaction in this fragmentLocationMessage to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
