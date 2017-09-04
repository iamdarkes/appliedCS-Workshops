/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static android.R.attr.button;
import static android.R.attr.key;
import static android.R.attr.label;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private FastDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String wordFragment = "";
    private final String WORD_FRAGMENT = "com.google.engedu.ghost.wordfragment";
    private final String TURN = "com.google.engedu.ghost.turn";

    private Button challengeButton;
    private Button resetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        resetButton = (Button) findViewById(R.id.restartButton);
        challengeButton = (Button) findViewById(R.id.challengeButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordFragment = "";
                onStart(view);
            }
        });

        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView label = (TextView) findViewById(R.id.gameStatus);

                Log.i("challengebword", wordFragment);


                if(wordFragment.length() >= GhostDictionary.MIN_WORD_LENGTH && dictionary.isWord(wordFragment)) {
                    Log.i("you won", "you won");
                    label.setText("Word fragment is long enough and is a word in the dictionary. You Win!");
                } else {
                    String s = dictionary.getAnyWordStartingWith(wordFragment);
                    if(s == null) {
                        label.setText("No more valid words can be made. You win!");
                    } else {
                        label.setText("Valid word: " + s + " can be made from word fragment. Computer wins.");
                    }
                }
            }
        });

        AssetManager assetManager = getAssets();
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        if(savedInstanceState != null) {
            wordFragment = savedInstanceState.getString(WORD_FRAGMENT);
            userTurn = savedInstanceState.getBoolean(TURN);
        }
        try {
            //dictionary = new SimpleDictionary(assetManager.open("words.txt"));
            dictionary = new FastDictionary(assetManager.open("words.txt"));



            Log.i("isword", dictionary.isWord("symbol") + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
            onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again




        //Log.i("c", dictionary.isWord(wordFragment) + "");
        if(wordFragment.length() >= GhostDictionary.MIN_WORD_LENGTH && dictionary.isWord(wordFragment) && !userTurn) {
            Log.i("t", (wordFragment.length() >= GhostDictionary.MIN_WORD_LENGTH) + "");
            Log.i("f", dictionary.isWord(wordFragment) + "");
            label.setText("Word length is long enough and is in dictionary. Victory for Computer.");
        }

            String s = dictionary.getAnyWordStartingWith(wordFragment);
            if(s == null) {
                label.setText("You have been challenged. There is no more words in the dictionary to build upon. Victory for Computer.");
            } else{
                wordFragment += s.substring(wordFragment.length(), wordFragment.length() + 1);
                Log.i("wordfra", wordFragment);
            }

        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText(wordFragment);


        userTurn = true;
       // label.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        //Log.i("keycode", keyCode + "");
        //Log.i("event", event.toString() + "");


        if(Character.isAlphabetic(event.getUnicodeChar())){
            TextView text = (TextView) findViewById(R.id.ghostText);
            wordFragment += (char) event.getUnicodeChar();
            text.setText(wordFragment);
            //Log.i("wordfrag", wordFragment);


            computerTurn();
        }
            return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(WORD_FRAGMENT, wordFragment);
        outState.putBoolean(TURN, userTurn);
    }
}
