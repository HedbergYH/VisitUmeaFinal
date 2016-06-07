package com.example.umyhnystma.matsvisitumea;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailInfoFragment extends Fragment {

    View view;
    ImageView imageview;
    TextView smalltext_in_fragment_detail_info,text_in_fragment_detail_info, title_in_fragment_detail_info;


    public DetailInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentLocationMessage
        view = inflater.inflate(R.layout.fragment_detail_info, container, false);
        smalltext_in_fragment_detail_info = (TextView) view.findViewById(R.id.smalltext_in_fragment_detail_info);
        smalltext_in_fragment_detail_info = (TextView) view.findViewById(R.id.smalltext_in_fragment_detail_info);
        title_in_fragment_detail_info = (TextView) view.findViewById(R.id.title_in_fragment_detail_info);

        setHasOptionsMenu(true);


/*
        Bundle bundle = getArguments();
        int position = bundle.getInt("KEY");

        String info = ((InfoDetailActivity)getActivity()).mySites.get(position).getName();

*/
        Bundle bundle2 = getArguments();

        Site mySite = (Site)bundle2.getSerializable("KEY_SERIALIZABLE");
        String objectTitle = mySite.getName();


        Log.i("TAG", "I FRAGMENTET. Borde synas");

        title_in_fragment_detail_info.setText(objectTitle);
        smalltext_in_fragment_detail_info.setText(mySite.getDescription());


         // Skall anropas separat för vaje bild
        String myPicture = mySite.getPictureURL();

        Log.i("TAG","myPicture = " + myPicture);

        ImageLoader myImageLoader = ImageLoader.getInstance();
        myImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageview = (ImageView)view.findViewById(R.id.pictureofbuilding);
        myImageLoader.displayImage(myPicture,imageview);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_detailinfo, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.track){

            //Starta trackfragmentet här
            Toast.makeText(getActivity(), "Clicked on track fragment start.", Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
