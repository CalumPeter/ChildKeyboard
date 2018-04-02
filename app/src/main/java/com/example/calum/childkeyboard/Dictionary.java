package com.example.calum.childkeyboard;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Dictionary class holds the list of words in the current dictionary.
 * Spell checking and Levenshtein are implemented from here.
 */

public class Dictionary {
	
	List<String> dictionary;
	AssetManager am;

	public Dictionary(AssetManager assets){
		am = assets;
		dictionary = createDict();

	}
	
	public List<String> getDict(){
		return dictionary;
	}

	/**
	 * Creates a dictionary of words from a text file.
	 *
	 * @return list of words.
	 */
	public List<String> createDict(){

		String fileName = "wlist_match10.txt";
        String line = null;
        List<String> list = new ArrayList<String>();

        try {

			InputStream is = am.open("wlist_match10.txt");
			InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isr);

            while((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }   

            bufferedReader.close();     
            
            return list;
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");
        }
		return list;
	}

	/**
	 * Creates a 2D array with all indexes equaling 0.
	 *
	 * @param i
	 * @param j
	 * @return empty dictionary initialised to zero.
	 */
	public int[][] createEmptyDict(int i, int j){
		
		int[][] dict = new int[i][j];
		
		for (int x = 0; x < i; x++){
			for (int y = 0; y < j; y++){
				dict[x][y] = 0;
			}
		}
		
		return dict;
		
	}

	/**
	 * Sets the prefix for levenshtein distance.
	 * Values for comparison of word with empty string.
	 *
	 * @param dict
	 * @param l1
	 * @param l2
	 * @return 2D array with empty string prefix.
	 */
	public int[][] setPrefix(int[][] dict, int l1, int l2){
		
		for (int i = 1; i < l1; i++){
			dict[i][0] = i;
		}
		
		for (int i = 0; i < l2; i++){
			dict[0][i] = i;
		}
		
		return dict;
	}

	/**
	 * Prints the 2D array of levenshtein distance.
	 * Used for testing in development.
	 *
	 * @param dist
	 * @param l1
	 * @param l2
	 */
	public void printDict(int[][] dist, int l1, int l2){
		for (int x = 0; x < l1; x++){
			for (int y = 0; y < l2; y++){
				System.out.print(dist[x][y] + " ");
			}
			System.out.println("\n");
		}
	}

	/**
	 *  Compares two strings and computes the Levenshtein Distance
	 *  between them.
	 *
	 * @param word1
	 * @param word2
	 * @return levenshtein distance between two strings.
	 */
	public int levenshteinDistance(String word1, String word2){
		
		String w1 = " " + word1;
		String w2 = " " + word2;
		
		int[][] dist = createEmptyDict(w1.length(), w2.length());
		
		dist = setPrefix(dist, w1.length(), w2.length());
		
		for (int j = 1; j < w2.length(); j++){
			for (int i = 1; i < w1.length(); i++ ){
				int subCost = 0;
				if (w1.charAt(i) == w2.charAt(j)){
					subCost = 0;
				}
				else{
					subCost = 1;
				}
				int minVal = Math.min(dist[i-1][j]+1, dist[i][j-1] +1);
				minVal = Math.min(minVal,dist[i-1][j-1] + subCost);
				dist[i][j] = minVal;
			}
		}
		
		return dist[w1.length()-1][w2.length()-1];
	}

	/**
	 * Takes in a word and returns a list of strings that are
	 * a distance of 1 away from it.
	 *
	 * @param word
	 * @return list of words close to inputted word.
	 */
	public List<String> checkWord(String word){
		
		List<String> nearMiss = new ArrayList<String>();

		if (!dictionary.contains(word.toLowerCase())) {
			for (String elem : dictionary) {
				if (levenshteinDistance(word.toLowerCase(), elem) < 2) {
					nearMiss.add(elem);
				}
			}
		}
		
		return nearMiss;
	}

}
