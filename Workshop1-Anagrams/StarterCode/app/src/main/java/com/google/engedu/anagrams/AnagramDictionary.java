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

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private List<String> wordList = new ArrayList<>();
    private Set<String> wordSet = new HashSet<>();
    private Map<String, List<String>> lettersToWord = new HashMap<>();
    private Map<Integer, List<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            List<String> values = new ArrayList<>();
            List<String> sizes = new ArrayList<>();

            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            String sw = sortLetters(word);
            if(lettersToWord.containsKey(sw)) {
                values = lettersToWord.get(sw);
            }
            values.add(word);
            lettersToWord.put(sw, values);

            if(sizeToWords.containsKey(word.length())) {
                sizes = sizeToWords.get(word.length());
            }
            sizes.add(word);
            sizeToWords.put(word.length(), sizes);
        }

    }

    public boolean isGoodWord(String word, String base) {

        return wordSet.contains(word) && !word.contains(base) ? true : false;

    }


    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        for(String word : wordList) {
            if(word.length() == targetWord.length()) {
                if(sortLetters(word).equals(sortLetters(targetWord))) {
                    result.add(word);
                    Log.i("Word Added: ", word);
                }
            }
        }

            return result;
    }

    private String sortLetters(String word) {
        char[] c = word.toCharArray();
        Arrays.sort(c);
        return new String(c);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        List<String> result = new ArrayList<String>();

        for(int i = 97; i < 123; i++) {
            String s = word + (char) i;
            s = sortLetters(s);
            if(isGoodWord(s, word)) {
                if (lettersToWord.containsKey(s)) {
                    List<String> temp = new ArrayList<String>();
                    temp.addAll(lettersToWord.get(s));

                    for (String st: temp) {
                        if(isGoodWord(st, word)) {
                            result.add(st);
                        }
                    }


                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithTwoMoreLetter(String word) {
        List<String> result = new ArrayList<String>();

        for(int i = 97; i < 123; i++) {
            for(int j = i; j < 123; j++) {
                String s = word + (char) i + (char) j ;
                s = sortLetters(s);
                if (isGoodWord(s, word)) {
                    if (lettersToWord.containsKey(s)) {
                        List<String> temp = new ArrayList<String>();
                        temp.addAll(lettersToWord.get(s));

                        for (String st : temp) {
                            if (isGoodWord(st, word)) {
                                result.add(st);
                            }
                        }


                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int numOfAnagrams = 0;
        String s = "";

        while (numOfAnagrams < MIN_NUM_ANAGRAMS) {
            List<String> result = sizeToWords.get(wordLength);
            s = result.get(random.nextInt(result.size()));
            result = getAnagramsWithOneMoreLetter(s);
            numOfAnagrams = result.size();


        }

        if(wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }
        return s;
    }

}
