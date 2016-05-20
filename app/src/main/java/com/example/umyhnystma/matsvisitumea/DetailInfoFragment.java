package com.example.umyhnystma.matsvisitumea;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_info, container, false);
        text_in_fragment_detail_info = (TextView) view.findViewById(R.id.text_in_fragment_detail_info);
        smalltext_in_fragment_detail_info = (TextView) view.findViewById(R.id.smalltext_in_fragment_detail_info);
        title_in_fragment_detail_info = (TextView) view.findViewById(R.id.title_in_fragment_detail_info);

        Bundle bundle = getArguments();
        int position = bundle.getInt("KEY");
        String info = ((InfoDetailActivity)getActivity()).mySites.get(position).getName();


        Log.i("TAG", "I FRAGMENTET. Borde synas");


        title_in_fragment_detail_info.setText(info);
        text_in_fragment_detail_info.setText(info);
        Log.i("TAG", info);


         // Skall anropas separat för vaje bild
        String myPicture = "http://blogg.vk.se/wp-content/uploads/oldblog/1589/images/saratunneln8(1).JPG";
        ImageLoader myImageLoader = ImageLoader.getInstance();
        myImageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageview = (ImageView)view.findViewById(R.id.pictureofbuilding);
        myImageLoader.displayImage(myPicture,imageview);

        return view;
    }




}
