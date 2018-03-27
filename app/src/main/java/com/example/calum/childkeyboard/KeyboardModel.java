package com.example.calum.childkeyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



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

    public String newSent(String sentence, String key){

        if (key.equals("<-") & sentence.length() > 0){
            return sentence.substring(0,sentence.length()-1);
        }
        else if(key.equals("<-") & sentence.length() == 0){
            return sentence;
        }
        else{
            return sentence+key;
        }
    }

}