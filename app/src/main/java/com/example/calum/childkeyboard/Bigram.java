package com.example.calum.childkeyboard;

public class Bigram {
	
	private int frequency;
	private String word1;
	private String word2;
	
	public Bigram(int f, String w1, String w2){
		frequency = f;
		word1 = w1;
		word2 = w2;
	}
	
	public int getFreq(){
		return frequency;
	}
	
	public String getWord1(){
		return word1;
	}
	
	public String getWord2(){
		return word2;
	}

}
