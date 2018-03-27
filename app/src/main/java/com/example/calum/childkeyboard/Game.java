package com.example.calum.childkeyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {

    private Map<String,Object> words;
    private String currentWord;
    private int counter;
    private int score;

    public Game(){
        words = new HashMap();
        counter = 0;
        currentWord="";
        score = 0;
        testCase();
    }

    public void testCase(){
        words.put("test",7);
        words.put("there",3);
        words.put("last",2);
        words.put("begin",10);
        words.put("welcome",12);
    }

    public void addWord(String w){

        if(!w.equals("")) {
            if (words.containsKey(w)) {
                words.put(w, (int) words.get(w) + 3);
            } else {
                words.put(w, 5);
            }
        }

    }

    public void resetCounter(){
        counter=0;
    }

    public int getScore(){
        return score;
    }

    public void printMap(){
        for (Map.Entry<String, Object> entry : words.entrySet()) {
            String key = entry.getKey();
            int value = (int) entry.getValue();
            System.out.println("Key: " + key + " Value: " + value);
        }
    }

    public void incrementScore(String w){
        score++;
        if((int)words.get(w) > 0) {
            words.put(w, (int) words.get(w) - 1);
        }
        printMap();
    }

    public void decrementScore(String w){
        if(score>0){
            score--;
        }

        words.put(w, (int)words.get(w)+1);
        printMap();
    }

    public int getCount(){
        return counter;
    }

    public void incrementCount(){
        if (counter == 2) {
            counter=0;
        }
        else{
            counter++;
        }
    }

    public String getWord(){
        return currentWord;
    }

    public void newWord(){

        if (counter == 0) {

            Random r = new Random();
            List<String> wordBucket = new ArrayList<String>();

            for (Map.Entry<String, Object> entry : words.entrySet()) {
                String key = entry.getKey();
                int value = (int) entry.getValue();
                for (int i = 0; i <= value; i++) {
                    wordBucket.add(key);
                }
            }

            currentWord = wordBucket.get(r.nextInt(wordBucket.size() - 1));
        }


    }

}