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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class TrieNode {
    public HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }


    public void add(String word) {
        TrieNode currentRoot = this;
        for(int i = 0; i < word.length(); i++){
            if(currentRoot.children.containsKey(word.charAt(i))){
                currentRoot = currentRoot.children.get(word.charAt(i));
            } else {
                TrieNode newTrieNode = new TrieNode();
                currentRoot.children.put(word.charAt(i), newTrieNode);
                currentRoot = newTrieNode;
            }
        }
        currentRoot.isWord = true;
    }

    public boolean isWord(String word) {
        TrieNode currentRoot = this;
        for(int i = 0; i < word.length(); i++) {
            if(currentRoot.children.containsKey(word.charAt(i))) {
                currentRoot = currentRoot.children.get(word.charAt(i));
            } else {
                return false;
            }
        }
        return currentRoot.isWord;
    }

    public String getAnyWordStartingWith(String prefix) {
        if(prefix.isEmpty()){
            Random rand = new Random();
            int randInt = rand.nextInt(26) + 97;
            prefix += (char)randInt;
            return prefix;
        }

        TrieNode currentRoot = this;
        for(int i = 0; i < prefix.length(); i++) {
            if(currentRoot.children.containsKey(prefix.charAt(i))) {
                currentRoot = currentRoot.children.get(prefix.charAt(i));
            } else {
                return null;
            }
        }

        while(!currentRoot.isWord) {
            List<Character> keys = new ArrayList<Character>(currentRoot.children.keySet());
            currentRoot = currentRoot.children.get(keys.get(0));
            prefix += keys.get(0);
        }
        return prefix;
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }


}

