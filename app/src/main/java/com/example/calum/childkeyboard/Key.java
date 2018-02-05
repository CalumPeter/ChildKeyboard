package com.example.calum.childkeyboard;

public class Key {

    String keyChar;
    int xco;
    int yco;

    public Key(String keyC, int x, int y){
        keyChar = keyC;
        xco = x;
        yco = y;
    }

    public String getChar(){
        return keyChar;
    }

    public void setXco(int x){
        xco = x;
    }

    public void setYco(int y){
        yco = y;
    }

    public int getXco(){
        return xco;
    }

    public int getYco(){
        return yco;
    }

}