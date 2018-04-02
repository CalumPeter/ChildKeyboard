package com.example.calum.childkeyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Word object holds the incorrectly spelt or grammatical error.
 * Holds the correction to these errors so when tapped can then
 * be corrected.
 */

public class Word {

    String word;
    List<String> possible;

    public Word(String w, List<String> ws){
        word = w;
        possible = ws;
    }

    public String getWord(){
        return word;
    }

    public List<String> getPossible(){
        return possible;
    }

} 