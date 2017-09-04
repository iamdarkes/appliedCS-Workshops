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

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private List<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }




    @Override
    public String getAnyWordStartingWith(String prefix) {

        if(prefix.isEmpty())
            return words.get(new Random().nextInt(words.size()));

        int l = 0;
        int r = words.size() - 1;
        int mid = r / 2;
        while(l < r) {
            String word = words.get(mid);

            if(word.length() > prefix.length() &&
                    word.substring(0,prefix.length()).equals(prefix)){
                Log.i("it", word);
                return word;
            }

            if(word.compareTo(prefix) > 0){
                r = mid - 1;
            } else {
                l = mid + 1;
            }
            mid = (r + l) / 2;
        }
        return null;
    }



    @Override
    public String getGoodWordStartingWith(String prefix) {
    return new String("");
    }

}
