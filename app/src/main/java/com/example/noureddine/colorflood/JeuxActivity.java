package com.example.noureddine.colorflood;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Noureddine on 19/11/2017.
 */

public class JeuxActivity extends AppCompatActivity {

    private Button retjeux;
    private Chronometer mChronometer;
    private int debut=0;
    private int fin=5;
    private int score=0;

    private BoardView boardView;
    private LinearLayout buttonsLayout;
    private Game game;
    private int colorsCount = 3, boardSize = 5;
    private Paint[] paint;
    private int[] colorsList;

    /*the clickable buttons' size*/
    private int buttonSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeux);

        final MediaPlayer mpButtonClick = MediaPlayer.create(this, R.raw.clicksound2);


        retjeux = (Button) findViewById(R.id.retourjeux);
        retjeux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(JeuxActivity.this, MainActivity.class);
                startActivity(a);
                mpButtonClick.start();


            }
        });

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.start();


        this.buttonsLayout = (LinearLayout) findViewById(R.id.buttonsDisplay);
        boardView = (BoardView) findViewById(R.id.boardLayout);

        initColors();
        startGame();

    }

    public void startGame() {

        setButtons(buttonsLayout);
        this.game = new Game(boardSize, colorsCount, paint);
        initBoard();

    }
    /**Initialize the bord*/
    public void initBoard(){

        boardView.setGame(game);
        boardView.setBoardSize(boardSize);
        boardView.initData();
    }

    /** load the colors from the resources*/
    public void initColors(){

        try{
            //Retrieve the colors from an array
            colorsList = getResources().getIntArray(R.array.boardColors);
            //convert the colors into paints to be used with drawRect()
            setPaints();
        }
        catch(Exception e){
            Log.e("MainActivity", "Error when loading colors");
        }
    }

    /**load the paints that will be used*/
    public void setPaints(){

        paint = new Paint[colorsList.length];

        for(int i = 0; i< colorsList.length; i++){
            paint[i] = new Paint();
            paint[i].setColor(colorsList[i]);
        }
        boardView.setPaint(paint);
    }

    /**add interactive 'buttons' under the board*/
    public void setButtons(LinearLayout layout){

        int i;

        setButtonSize();

        /*create the given number of buttons and add them to the layout */
        for(i=0; i< colorsCount; i++){

            //create a new button
            final Button aButton = new Button(this);
            //set the button's color
            aButton.setBackgroundColor(colorsList[i]);
            //aButton.getBackground().setColorFilter(colorsList[i], PorterDuff.Mode.MULTIPLY);
            /*initialize the button's parameters for the layout*/
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(5, 0, 5, 0);
            params.height = buttonSize;
            params.width = buttonSize;
            aButton.setLayoutParams(params);

            final MediaPlayer mpButtonClick = MediaPlayer.create(this, R.raw.clicksound3);

            final TextView tx= (TextView) findViewById(R.id.click);
            debut=0;
            tx.setText(debut + " / " + fin);

            aButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //game.setSelectedColor(getClickedColor(aButton));
                    game.checkNeighbor(getClickedColor(aButton));
                    boardView.setGame(game);
                   mpButtonClick.start();
                    //check if the game is finished
                    debut++;
                    tx.setText(debut + " / " +fin);
                    checkEndGame();
                }
            });
            //add the button to the layout
            layout.addView(aButton);
        }
    }
    /**Check if the game has ended : if the player won or if he lost*/
    public void checkEndGame() {

        /*if the player got all the cells*/
        if (game.getGameStatus()){
            //display the "WIN" dialog
            endGameDialog(1);
            mChronometer.stop();
        }

        /*else if the player lost ......*/
        else if (debut==fin){
            endGameDialog(0);
            mChronometer.stop();
        }}

    /**create a dialog window when the game is finished
     * @param result : this is the game's result, 1 if the player won, 0 if he lost*/
    public void endGameDialog(int result) {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);////////////////////
        dialog.setContentView(R.layout.end_choice_layout);
        dialog.setCancelable(false);//can't dismiss the dialog by clicking outside

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//////////

        TextView text = (TextView) dialog.findViewById(R.id.text);
        TextView vScore = (TextView) dialog.findViewById(R.id.score);

        Button replayButton =(Button) dialog.findViewById(R.id.buttonReplay);
        Button nextButton = (Button) dialog.findViewById(R.id.buttonNext);

        // if the "replay" button is clicked, then reload the game
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replayLevel();
                dialog.dismiss();
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.stop();
                mChronometer.start();
            }
        });

        /*In case of a win load the win texts in the dialog*/
        if(result == 1){

            text.setText(getString(R.string.you_win));
            score();
            String str = Integer.toString(this.score);
            vScore.setText(str);/////
            vScore.append(" points");
            // if the "next" button is clicked, go to next level
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextLevel();
                    dialog.dismiss();
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.stop();
                    mChronometer.start();

                }
            });
        }
        /*In case we lose, set the appropriate dialog title (lose),
         keep the "replay" button and hide the "next" one*/
       else if (result == 0){

            text.setText(getString(R.string.you_lose));
            vScore.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        }

        dialog.show();
    }

    /**reload the board with the same size and the same amount of colors to replay the game*/
    public void replayLevel(){

        this.buttonsLayout.removeAllViews();/*delete the buttons so we can recreate an amount that*
        matches the new amount of colors, otherwise the buttons would add up on the layout */

        //start a new game
        startGame();

    }

    /**we got to the next level : more colors and a bigger board*/
    public void nextLevel(){
        int m = fin-debut;
        this.debut=0;
        this.fin+=10+m;

        this.boardSize += 5;//add 5 more cells horizontally and vertically
        this.colorsCount ++;//add 1 more color
        this.buttonsLayout.removeAllViews();/*delete the buttons so we can recreate an amount that*
        matches the new amount of colors*/

         /*make sure we can't have more than 7 colors*/
        if(colorsCount > 7) colorsCount--;
        /*make sure we can't have a width greater than 25 cells*/
        if(boardSize > 25) boardSize = 25;
        startGame();
    }

    public void score(){
        int m = fin-debut;
        this.score+=(m*100)+100;

    }

    /**When the user clicks a button we retrieve that button's color */
    public int getClickedColor(Button button){

        /*get the button's color*/
        ColorDrawable buttonColor = (ColorDrawable) button.getBackground();
        //int colorId = buttonColor.getColor();
        //Log.i("result",""+buttonColor);

        /*return it as an int*/
        return buttonColor.getColor();
    }

    /**get the screen's width*/
    public int getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     * generate a random int between 0 and n-1
     * */
    public int getRand(int n){
        Random rand = new Random();
        return rand.nextInt(n);
    }

    /**set the buttons' size according to screen's size*/
    public void setButtonSize(){
        int a;
        a = getScreenSize();
        this.buttonSize =  (a / this.colorsCount) - 30;
    }
}







