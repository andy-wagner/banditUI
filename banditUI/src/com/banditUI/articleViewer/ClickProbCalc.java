package system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class ClickProbCalc {

	private static Dictionaries dictionaries = Dictionaries.getInstance();
    
    public static double CalcFeatureVectorScore( String articlePath, HashMap<String,Double> FeatureVector ) {
    	double score = 0;
    	
    	// Read the article, and get the appropriate dictionary.
    	Article a = new Article(articlePath);
    	HashMap<String, Double> dictionary = dictionaries.getDictionary(a.getTopic());
    	if (dictionary == null) { 
    		System.out.println("Unable to extract topic from article at ");
    		System.out.println(articlePath);
    		return -1;
    	}
    	
    	// Now read through the article and count up the number of times each word
    	// in the dictionary appears.
    	HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();
    	WordCounter.CountWords(a.getStory(), wordCounts);
    	for (String word : wordCounts.keySet()) {
    		if (dictionary.containsKey(word))
    			score += (wordCounts.get(word)*dictionary.get(word)*FeatureVector.get(word));
    	}
    	
    	return score;
    }
	
    

}
