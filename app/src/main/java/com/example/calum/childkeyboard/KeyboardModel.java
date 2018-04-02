package com.example.calum.childkeyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Keyboard Model holds a list of keys.
 * The class deals with the touches on the keyboard view.
 */

public class KeyboardModel {

    List<Key> keys = new ArrayList<Key>();
    String [] firstRow = new String[]{"q","w","e","r","t","y","u","i","o","p"};
    String [] secondRow = new String[]{"a","s","d","f","g","h","j","k","l"};
    String [] thirdRow = new String[]{".","z","x","c","v","b","n","m","<-"};


    public String getFirstRowElem(int i){
        return firstRow[i];
    }

    public String getSecondRowElem(int i){
        return secondRow[i];
    }

    public String getThirdRowElem(int i){
        return thirdRow[i];
    }

    public void createKey(int xco, int yco, String c){
        Key k = new Key(c, xco, yco);
        keys.add(k);
    }

    /**
     * Coordinates are taken in and the closest key is returned.
     *
     * @param xco
     * @param yco
     * @return closest key
     */
    public String getKey(int xco, int yco){

        double minDistance = Double.MAX_VALUE;
        String selectedKey = null;

        for (Key k : keys){
            double distance = Math.hypot((xco - k.getXco()), yco - k.getYco());
            if (minDistance > distance){
                selectedKey = k.getChar();
                minDistance = distance;
            }
        }

        if (selectedKey.equals(".")){
            return ". ";
        }

        return selectedKey.toLowerCase();
    }

    /**
     * Takes in a string and adds the key value to it.
     *
     * @param sentence
     * @param key
     * @return updated sentence.
     */
    public String newSent(String sentence, String key){

        //If backspace is hit a letter is taken off of the string.
        if (key.equals("<-") & sentence.length() > 0){
            return sentence.substring(0,sentence.length()-1);
        }
        //Nothing happens if string is empty.
        else if(key.equals("<-") & sentence.length() == 0){
            return sentence;
        }
        else{
            return sentence+key;
        }
    }

}