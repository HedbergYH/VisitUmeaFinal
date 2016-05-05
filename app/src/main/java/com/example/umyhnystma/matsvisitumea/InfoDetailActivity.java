package com.example.umyhnystma.matsvisitumea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoDetailActivity extends AppCompatActivity {

    TextView myText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);

        myText = (TextView)findViewById(R.id.textInfoDetailActivity);
        myText.setText("I den nya InfoDetailActivity");

    }
}
