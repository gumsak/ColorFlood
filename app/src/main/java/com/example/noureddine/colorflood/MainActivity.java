package com.example.noureddine.colorflood;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.example.noureddine.colorflood.R.drawable.son;

public class MainActivity extends AppCompatActivity {
    private ImageButton info;
    private ImageButton score;
    private Button play;
    private ToggleButton sound;
    private boolean checked;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer mpButtonClick = MediaPlayer.create(this, R.raw.clicksound);


        info = (ImageButton) findViewById(R.id.about);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(a);
                mpButtonClick.start();


            }
        });

        score = (ImageButton) findViewById(R.id.score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(b);
                mpButtonClick.start();

            }
        });


        play = (Button) findViewById(R.id.start1);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent c = new Intent(MainActivity.this, JeuxActivity.class);
                startActivity(c);
                mpButtonClick.start();


            }
        });

        sound = (ToggleButton) findViewById(R.id.soundbutton);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked = ((ToggleButton) view).isChecked();
                if (checked) {

                    sound.setBackgroundResource(R.drawable.sonc);
                    ;
                    Toast.makeText(getApplicationContext(), "Sound_Off", Toast.LENGTH_LONG).show();


                } else {

                    sound.setBackgroundResource(R.drawable.son);
                    Toast.makeText(getApplicationContext(), "Sound_On", Toast.LENGTH_LONG).show();
                }
            }
        });
      }}

















