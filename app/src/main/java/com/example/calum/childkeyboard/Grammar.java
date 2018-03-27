package com.example.calum.childkeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grammar {

	List<List<String>> grams;
	
	public Grammar(){
		grams = new ArrayList<List<String>>();
		createGrammars();
	}
	
	public void createGrammars(){
		grams.add(getThere());
		grams.add(getTo());
		grams.add(getThen());
		grams.add(getSelf());
	}
	
	public List<String> getThen(){
		List<String> thenGrammar = new ArrayList<String>(Arrays.asList("then","than"));
		return thenGrammar;
	}
	
	public List<String> getSelf(){
		List<String> selfGrammar = new ArrayList<String>(Arrays.asList("me","myself","i"));
		return selfGrammar;
	}
	
	public List<String> getTo(){
		List<String> toGrammar = new ArrayList<String>(Arrays.asList("to","too","two"));
		return toGrammar;
	}
	
	public List<String> getThere(){
		List<String> thereGrammar = new ArrayList<String>(Arrays.asList("there","their","they're"));
		return thereGrammar;
	}
	
	public List<List<String>> getGrammars(){
		return grams;
	}
	
}
