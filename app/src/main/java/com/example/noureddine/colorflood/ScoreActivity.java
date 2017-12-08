package com.example.noureddine.colorflood;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Noureddine on 19/11/2017.
 */

public class ScoreActivity extends AppCompatActivity {

    private Button retscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        final MediaPlayer mpButtonClick = MediaPlayer.create(this, R.raw.clicksound2);

        retscore=(Button) findViewById(R.id.retourscore);
        retscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(a);
                mpButtonClick.start();



            }});


    }}
