package com.example.umyhnystma.matsvisitumea;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoDetailActivity extends AppCompatActivity {

    TextView myText;



    public static final String INTENT_TAB_NUMBER = "INTENT_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);

        final ImageView mapImage = (ImageView) findViewById(R.id.mapImage);
        final ImageView listImage = (ImageView) findViewById(R.id.listImage);

        mapImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mapImage.setColorFilter(Color.argb(50, 0, 0, 0));
                listImage.setColorFilter(Color.argb(0, 0, 0, 0));
                return false;
            }
        });

        listImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listImage.setColorFilter(Color.argb(50, 0, 0, 0));
                mapImage.setColorFilter(Color.argb(0, 0, 0, 0));
                return false;
            }
        });
    }

    public void onBackPressed(){

      //  Intent intent = new Intent(getActivity(), InfoDetailActivity.class);     // Anropar under runtime class-filen
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(INTENT_NOTE_STRING, currentNote.note);                 // Sträng skickas med bundle
        intent.putExtra(INTENT_TAB_NUMBER, 1);                         // Position skickas med bundle
        this.startActivity(intent);



        Log.i("MIN_TAG", "onBackPressed i infoDetailActivity");
        super.onBackPressed();
    }


    @Override
    public void onResume(){
        super.onResume();

        Log.i("MIN_TAG","onResume i infoDetailActivity");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("MIN_TAG","onPause i infoDetailActivity");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MIN_TAG","onDestroy i infoDetailActivity");
    }


}
