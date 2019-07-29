package com.ton.jacky.simon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Observer, View.OnClickListener{
    Model mModel;
    //array of buttons that correspond to buttons in game
    Button buttons[] = new Button[6];
    //button to return to the main menu
    Button menuButton;
    //textbox for user info
    TextView infoText;
    //textbox for the score
    TextView scoreText;
    //delay in ms for computer and user animations
    int delay;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mModel = Model.getInstance();
        mModel.addObserver(this);
        mModel.initObservers();
        //init the buttons and text boxes
        buttons[0] = findViewById(R.id.button1);
        buttons[1] = findViewById(R.id.button2);
        buttons[2] = findViewById(R.id.button3);
        buttons[3] = findViewById(R.id.button4);
        buttons[4] = findViewById(R.id.button5);
        buttons[5] = findViewById(R.id.button6);
        menuButton = findViewById(R.id.menuButton);
        infoText = findViewById(R.id.infoText);
        scoreText = findViewById(R.id.scoreText);
        delay = 4000 - (1200) * mModel.getDifficulty();
        setButtons(State.VIEWABLE);
        menuButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, MainMenu.class);

                // Start activity
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        //if the player lost, start the next Round
        if (mModel.getState() == State.LOSE){
            mModel.newRound();
            //during the computers turn buttons are unclickable
            setButtons(State.UNCLICKABLE);
            roundComputer();
        }
        //if the player Won, start the next Round
        else if (mModel.getState() == State.WIN){
            mModel.newRound();
            //during the computers turn buttons are unclickable
            setButtons(State.UNCLICKABLE);
            roundComputer();
        }
        //If it's the players turn to repeat the sequence
        else if (mModel.getState() == State.HUMAN){
            //set the menu button to invisible during players turn
            int theButton = Integer.parseInt(((Button)v).getText().toString());
            int animSpeed = delay;
            //simple flash animation for buttons
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(animSpeed); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(0);
            v.startAnimation(anim);
            mModel.verifyButton(theButton);
            //if the user does not input the correct sequence turn on the menu button
            if (mModel.getState() == State.LOSE){
                menuButton.setVisibility(View.VISIBLE);
            }
        }
        roundText();
    }

    //toggle the buttons between 3 different states
    //visible, clickable, or unclickable
    public void setButtons(State type){
        int numButtons = mModel.getNumButtons();
        if (type == State.VIEWABLE) {
            for (int i = 0; i < numButtons; ++i) {
                buttons[i].setVisibility(View.VISIBLE);
            }
            for (int i = numButtons; i < 6; ++i) {
                buttons[i].setVisibility(View.GONE);
            }
        }
        if (type == State.CLICKABLE) {
            for (int i = 0; i < numButtons; ++i) {
                buttons[i].setClickable(true);
            }
        }
        if (type == State.UNCLICKABLE) {
            for (int i = 0; i < numButtons; ++i) {
                buttons[i].setClickable(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove observer when activity is destroyed.
        mModel.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    //animation for the computer sequence
    public void compAnimation(){
            int _button = mModel.nextButton();
            int animSpeed = delay;
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(animSpeed);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(0);
            buttons[_button].startAnimation(anim);
    }
    //function to move through the computer sequence
    public void roundComputer() {
        menuButton.setVisibility(View.INVISIBLE);
        if (mModel.getState() == State.COMPUTER) {
            timer.schedule(new TimerTask() {
                public void run() {
                    compAnimation();
                    roundComputer();
                }
            }, delay);
        }
        if (mModel.getState() != State.COMPUTER) {
            timer.schedule(new TimerTask() {
                public void run() {
                    //After the computer has finished it's sequence the buttons are clickable
                    setButtons(State.CLICKABLE);
                }
            }, delay);
        }
    }
    //set the ingame text (score and info)
    public void roundText() {
        switch (mModel.getState()) {
            case START:
                infoText.setText("Tap a Button to Start");
                break;
            case WIN:
                infoText.setText("You Won! Tap to Continue");
                break;
            case LOSE:
                infoText.setText("You Lost! Tap to Play Again");
                break;
            case COMPUTER:
                infoText.setText("Watch what I do ...");
                break;
            case HUMAN:
                infoText.setText("Your Turn ...");
                break;
        }
        scoreText.setText("Score:" + Integer.toString(mModel.getScore()));
    }
}
