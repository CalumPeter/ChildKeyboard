package com.example.calum.childkeyboard;

/**
 * Ngram class holds the list of Bigrams created for grammar.
 * NGram also deals with grammar corrections.
 */

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ngram {

	private List<Bigram> listOfBigrams;
	Grammar grammar;
	AssetManager assets;
	
	public Ngram(AssetManager am){
		assets = am;
		grammar = new Grammar();
		listOfBigrams = createBigrams();
	}
	
	public List<Bigram> getBigrams(){
		return listOfBigrams;
	}

	/**
	 * The inputted sentence is checked for grammar errors.
	 * If any are found they are corrected and the corrected
	 * sentence is returned.
	 *
	 * @param sentence
	 * @return Corrected Sentence
	 */
	public String checkSentence(String sentence){
		
		String[] splitSent = sentence.split(" ");
		
		String newSent = "";
		
		for (int i = 0; i < splitSent.length; i++){
			String bestB = "";
			int best = 0;
			//check each list of common grammatical errors.
			for (List<String> grammar : grammar.getGrammars()){
				
				if (grammar.contains(splitSent[i])){
					//Go through Bigrams and get most frequent.
					for(Bigram b : getBigrams()){
						for (String gram : grammar){
							if (i != splitSent.length-1){
								if(b.getWord1().equals(gram) && b.getWord2().equals(splitSent[i+1])){
										
										if (b.getFreq()>best){
											best = b.getFreq();
											bestB = b.getWord1();
										}
									}
								}
							if (i != 0){
								if(b.getWord1().equals(splitSent[i-1]) && b.getWord2().equals(gram)){
									
									if (b.getFreq()>best){
										best = b.getFreq();
										bestB = b.getWord2();
									}
								}
							}
							}
						}
						
					}
				}
				if (bestB!=""){
					newSent = newSent + " " + bestB;
				}
				else{
					newSent= newSent + " " + splitSent[i];
				}
			}
		
		return newSent.trim();
	}

	/**
	 *  Reads in a text file and parses it to create
	 *  a list of Bigrams.
	 *
	 * @return list of Bigram objects
	 */
	public List<Bigram> createBigrams(){
		
		String fileName = "grammarlist2.txt";
		List<Bigram> list = new ArrayList<Bigram>();
        String line = null;

        try {
			InputStream is = assets.open("grammarlist2.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(isr);

            while((line = bufferedReader.readLine()) != null) {
                String[] a = line.split("\t");
                Bigram b = new Bigram(Integer.parseInt(a[0]), a[1], a[2]);
                list.add(b);
            }   

            bufferedReader.close();
            
        
		
            return list;
		
        }
        catch(IOException io){
        	System.out.println(
                    "Error reading file '" 
                    + fileName + "'");
        }
        return list;
	}
	
}
