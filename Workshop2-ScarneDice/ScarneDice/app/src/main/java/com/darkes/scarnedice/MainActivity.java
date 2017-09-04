package com.darkes.scarnedice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final int MIN_WINNING_SCORE = 100;
    int mUserOverallScore = 0;
    int mUserTurnScore = 0;
    int mComputerOverallScore = 0;
    int mComputerTurnScore = 0;

    TextView mScoreTextView;
    ImageView mDieImageView;
    Button mRollButton;
    Button mHoldButton;
    Button mResetButton;

    int[] die = { R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6 } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScoreTextView = (TextView) findViewById(R.id.score_text_view);
        mDieImageView = (ImageView) findViewById(R.id.die_image_view);
        mRollButton =  (Button) findViewById(R.id.roll_button);
        mHoldButton =  (Button) findViewById(R.id.hold_button);
        mResetButton =  (Button) findViewById(R.id.reset_button);

        updateScores();

        mRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = new Random().nextInt(6);
                if (num != 0)
                    mUserTurnScore += num + 1;
                else {
                    mUserTurnScore = 0;
                    computerTurn();
                }

                mDieImageView.setImageResource(die[num]);
                waitToUpdate();
            }
        });

        mHoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserOverallScore += mUserTurnScore;
                mUserTurnScore = 0;

                computerTurn();
                waitToUpdate();
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetScores();
                updateScores();
            }
        });
    }

    private void resetScores() {
        mUserTurnScore = 0;
        mUserOverallScore = 0;
        mComputerTurnScore = 0;
        mComputerOverallScore = 0;
    }

    public void computerTurn() {
        mRollButton.setEnabled(false);
        mHoldButton.setEnabled(false);

            mScoreTextView.setText(R.string.computer_turn);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScoreTextView.setText(R.string.computer_turn);

                    int minCompScore = 20;


                        Log.d("Inside runnable", mComputerTurnScore + "");
                        //while( mComputerTurnScore < minCompScore) {
                        int num = new Random().nextInt(6);

                        if (num != 0) {
                            mComputerTurnScore += num + 1;
                            mDieImageView.setImageResource(die[num]);
                        } else {
                            mComputerTurnScore = 0;
                        }


                        mComputerOverallScore += mComputerTurnScore;
                        mComputerTurnScore = 0;


                        //enable buttons after 500ms
                        mRollButton.setEnabled(true);
                        mHoldButton.setEnabled(true);
                    }
            }, 1000);
    }


    public void waitToUpdate() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //update after 500ms
                updateScores();
            }
        }, 1500);
    }

    public void updateScores() {
        mScoreTextView.setText(String.format(getString(R.string.score_text), mUserOverallScore , mComputerOverallScore));
    }
}
