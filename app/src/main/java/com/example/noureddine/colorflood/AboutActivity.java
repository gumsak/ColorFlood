package com.example.noureddine.colorflood;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class AboutActivity extends AppCompatActivity {

    private Button ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final MediaPlayer mpButtonClick = MediaPlayer.create(this, R.raw.clicksound2);


        ret=(Button) findViewById(R.id.retourabout);

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(AboutActivity.this, MainActivity.class);
                startActivity(a);
                mpButtonClick.start();


            }
        });

    }
}





