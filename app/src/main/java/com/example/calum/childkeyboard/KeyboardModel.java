package com.example.calum.childkeyboard;

import java.util.List;
import java.util.ArrayList;

public class KeyboardModel {

    List<Key> keys = new ArrayList<Key>();
    String [] firstRow = new String[]{"Q","W","E","R","T","Y","U","I","O","P"};
    String [] secondRow = new String[]{"A","S","D","F","G","H","J","K","L"};
    String [] thirdRow = new String[]{"Z","X","C","V","B","N","M"};


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

        return selectedKey;
    }

}