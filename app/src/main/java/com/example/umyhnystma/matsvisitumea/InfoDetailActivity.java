package com.example.umyhnystma.matsvisitumea;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class InfoDetailActivity extends AppCompatActivity {

    TextView myText;
    public static final String INTENT_TAB_NUMBER = "INTENT_NUMBER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);

        myText = (TextView)findViewById(R.id.textInfoDetailActivity);
        myText.setText("I den nya InfoDetailActivity");



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
