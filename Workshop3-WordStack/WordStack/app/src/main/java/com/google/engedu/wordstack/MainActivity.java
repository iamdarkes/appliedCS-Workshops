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

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private Stack<LetterTile> placedTiles;
    private String word1, word2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();

                //store words in a arraylist
                /**
                 **
                 **  YOUR CODE GOES HERE
                 **
                 **/
                if(word.length() == WORD_LENGTH)
                    words.add(word);
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }



        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);

        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());

        placedTiles = new Stack<>();

    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();

                tile.moveToViewGroup((ViewGroup) v);

                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                /**
                 **
                 **  YOUR CODE GOES HERE
                 **
                 **/

                placedTiles.push(tile);

                return true;

            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    /**
                     **
                     **  YOUR CODE GOES HERE
                     **
                     **/


                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }

    protected boolean onStartGame(View view) {
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/

        reset();
   //     verticalLayout.removeAllViews();
        //stackedLayout.clear();
//        verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
//        stackedLayout = new StackedLayout(this);
//        verticalLayout.addView(stackedLayout, 3);

        do {
            word1 = words.get(random.nextInt(words.size()));
            word2 = words.get(random.nextInt(words.size()));

        } while (word1.equals(word2));

        Log.i("word1", word1);
        Log.i("word2", word2);


        int word1Counter = 0;
        int word2Counter = 0;

        String scrambled = "";

        while(word1Counter != word1.length() || word2Counter != word2.length()) {
            if(random.nextInt(2) == 0) {
                if(word1Counter != word1.length()) {
                    scrambled += word1.charAt(word1Counter);
                    word1Counter++;
                }
            } else {
                if (word2Counter != word2.length()) {
                    scrambled += word2.charAt(word2Counter);
                    word2Counter++;
                }
            }
        }
        Log.i("c1", "" + word1Counter);
        Log.i("c2", "" + word2Counter);

        Log.i("sword", scrambled);

        for (int i = scrambled.length() - 1; i >= 0; i--) {
            //Log.i("char", scrambled.charAt(i) + "");
            //LetterTile lt = new LetterTile(this, scrambled.charAt(i));
            stackedLayout.push(new LetterTile(this, scrambled.charAt(i)));
            //stackedLayout.push(lt);
        }



//        for(char c : scrambled.toCharArray()) {
//            LetterTile lt = new LetterTile(this, c);
//            stackedLayout.push(lt);
//        }
        messageBox.setText(scrambled);

        return true;
    }

    protected boolean onUndo(View view) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/

        LetterTile popped = placedTiles.pop();
        popped.moveToViewGroup(stackedLayout);
        return true;
    }


    private void reset() {
        LinearLayout word1 = (LinearLayout) findViewById(R.id.word1);
        LinearLayout word2 = (LinearLayout) findViewById(R.id.word2);

        word1.removeAllViews();
        word2.removeAllViews();
        stackedLayout.clear();
    }


}
