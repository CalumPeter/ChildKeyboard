package com.example.calum.childkeyboard;

import java.util.ArrayList;
import java.util.List;

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