package com.example.calum.childkeyboard;

import java.util.List;
import java.util.ArrayList;

/**
 * Sentence class is used to hold a list of incorrectly spelt
 *  words or grammar mistakes. Holds these in a list of Word objects.
 */

public class Sentence {

    private List<Word> sent;

    public Sentence(){
        sent = new ArrayList<Word>();
    }

    public void addWord(String word, List<String> words){
        Word w = new Word(word,words);
        sent.add(w);
    }

    public void clearSentence(){
        sent.clear();
    }

    public void addGram(String word, String gram){
        List<String> words = new ArrayList<String>();
        words.add(gram);
        Word w = new Word(word,words);
        sent.add(w);
    }

    public List<Word> getSentence(){
        return sent;
    }

} 