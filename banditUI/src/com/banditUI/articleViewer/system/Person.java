package com.banditUI.articleViewer.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Person {
	
	// Emphasis function
	private double alpha;
	private double beta;
    
	public static final int ENTERTAINMENT = 0;
	public static final int HEALTH = 1;
	public static final int MONEYWATCH = 2;
	public static final int POLITICS = 3;
	public static final int SPORTS = 4;
	public static final int TECH = 5;
	public static final int US = 6;
	public static final int WORLD = 7;
	
    private static final int DICT_SIZE_ENTERTAINMENT = 597;
    private static final int DICT_SIZE_HEALTH        = 323;
    private static final int DICT_SIZE_MONEYWATCH    = 334;
    private static final int DICT_SIZE_POLITICS      = 478;
    private static final int DICT_SIZE_SPORTS        = 420;
    private static final int DICT_SIZE_TECH          = 582;
    private static final int DICT_SIZE_US            = 510;
    private static final int DICT_SIZE_WORLD         = 435;

    //
    // General dispositions for each possible article topic.
    // These can take on values in the range [0,1]
    private double Entertainment_Disposition = 1;
    private double Health_Disposition = 1;
    private double MoneyWatch_Disposition = 1;
    private double Politics_Disposition = 1;
    private double Sports_Disposition = 1;
    private double Tech_Disposition = 1;
    private double US_Disposition = 1;
    private double World_Disposition = 1;

    //
    // Feature vectors for each article topic.
    // Each vector is the corresponding DICT_SIZE in length
    // and each entry is in the range [0, 1]
    private HashMap<String, Double> Entertainment_Feature_Vector;    
    private HashMap<String, Double> Health_Feature_Vector;
    private HashMap<String, Double> MoneyWatch_Feature_Vector;
    private HashMap<String, Double> Politics_Feature_Vector;
    private HashMap<String, Double> Sports_Feature_Vector;
    private HashMap<String, Double> Tech_Feature_Vector;
    private HashMap<String, Double> US_Feature_Vector;
    private HashMap<String, Double> World_Feature_Vector;

    
    public Person(boolean random) {
    	// 
    	// Either read in dispositions or generate them randomly.
    	if (random) { 
    		RandomizeDispositions(); 
    	}
    	else { 
    		ReadDispositions(); 
    	}
    	
    	//
    	// Randomly fill in feature vectors.
        FillFeatureVectors();
    }   
    
    public Person(double[] disps, double a, double b) {
        Entertainment_Disposition = disps[0];
        Health_Disposition = disps[1];
        MoneyWatch_Disposition = disps[2];
        Politics_Disposition = disps[3];
        Sports_Disposition = disps[4];
        Tech_Disposition = disps[5];
        US_Disposition = disps[6];
        World_Disposition = disps[7];
        alpha = a;
        beta = b;
        FillFeatureVectors();
    }
    
    private void FillFeatureVectors() {
      	Entertainment_Feature_Vector = new HashMap<String, Double>();
        Health_Feature_Vector = new HashMap<String, Double>();
        MoneyWatch_Feature_Vector = new HashMap<String, Double>();
        Politics_Feature_Vector = new HashMap<String, Double>();
        Sports_Feature_Vector = new HashMap<String, Double>(); 
        Tech_Feature_Vector = new HashMap<String, Double>();
        US_Feature_Vector = new HashMap<String, Double>();
        World_Feature_Vector = new HashMap<String, Double>();
        
        ArrayList<HashMap<String, Double>> FeatureVectors = new ArrayList<HashMap<String, Double>>();
        FeatureVectors.add(Entertainment_Feature_Vector);
        FeatureVectors.add(Health_Feature_Vector);
        FeatureVectors.add(MoneyWatch_Feature_Vector);
        FeatureVectors.add(Politics_Feature_Vector);
        FeatureVectors.add(Sports_Feature_Vector);
        FeatureVectors.add(Tech_Feature_Vector);
        FeatureVectors.add(US_Feature_Vector);
        FeatureVectors.add(World_Feature_Vector);
        
        Dictionaries dictionaries = Dictionaries.getInstance();
        String[] Topics = {"Entertainment", "Health", "MoneyWatch", "Politics",
        		"Sports", "Tech", "US", "World" };
        
        // 
        // Fill in each feature vector with random doubles in [0, 1)
        for (int i = 0; i < Topics.length; i++) {
        	HashMap<String, Double> dict = dictionaries.getDictionary(Topics[i]);
        	HashMap<String, Double> features = FeatureVectors.get(i);
        	for (String key : dict.keySet()) {
        		features.put(key, Math.random());
        	}
        }
    }
 
    private void RandomizeDispositions() {
    	//
    	// Fill each Disposition with a random double in [0, 1)
    	Entertainment_Disposition = Math.random();
        Health_Disposition = Math.random();
        MoneyWatch_Disposition = Math.random();
        Politics_Disposition = Math.random();
        Sports_Disposition = Math.random();
        Tech_Disposition = Math.random();
        US_Disposition = Math.random();
        World_Disposition = Math.random();
        beta = Math.random() * 0.8; // Max beta value of 0.8.
        alpha = Math.random() * beta; // Alpha no larger than beta.
    }

    private void ReadDispositions() {
        Entertainment_Disposition = 1;
        Health_Disposition = 1;
        MoneyWatch_Disposition = 1;
        Politics_Disposition = 1;
        Sports_Disposition = 1;
        Tech_Disposition = 1;
        US_Disposition = 1;
        World_Disposition = 1;
    	Scanner s = new Scanner(System.in);
    	System.out.println("Enter prior dispositions in the range [0, 1) for :");
    	
		while (Entertainment_Disposition >= 1) {
			System.out.print("Entertainment : ");
			Entertainment_Disposition = s.nextDouble();
		}
		while (Health_Disposition >= 1) {
			System.out.print("Health : ");
			Health_Disposition = s.nextDouble();
		}
		while (MoneyWatch_Disposition >= 1) {
			System.out.print("MoneyWatch : ");
			MoneyWatch_Disposition = s.nextDouble();
		}
		while (Politics_Disposition >= 1) {
			System.out.print("Politics : ");
			Politics_Disposition = s.nextDouble();
		}
		while (Sports_Disposition  >= 1) {
			System.out.print("Sports : ");
			Sports_Disposition = s.nextDouble();
		}
		while (Tech_Disposition  >= 1) {
			System.out.print("Tech : ");
			Tech_Disposition = s.nextDouble();
		}
		while (US_Disposition  >= 1) {
			System.out.print("US : ");
			US_Disposition = s.nextDouble();
		}
		while (World_Disposition  >= 1) {
			System.out.print("World : ");
			World_Disposition = s.nextDouble();
		}
		beta = 1;
		while (beta >= 0.8) {
			System.out.print("Emphasis beta : ");
			beta = s.nextDouble();
		}
		alpha = 1;
		while (alpha > beta) {
			System.out.print("Emphasis alpha : ");
			alpha = s.nextDouble();
		}
    }
    
    public HashMap<String, Double> getFeatureVector(int topic) {
    	switch (topic) {
    	case ENTERTAINMENT:
    		return Entertainment_Feature_Vector;
    	case HEALTH:
    		return Health_Feature_Vector;
    	case MONEYWATCH:
    		return MoneyWatch_Feature_Vector;
    	case POLITICS:
    		return Politics_Feature_Vector;
    	case SPORTS:
    		return Sports_Feature_Vector;
    	case TECH:
    		return Tech_Feature_Vector;
    	case US:
    		return US_Feature_Vector;
    	case WORLD:
    		return World_Feature_Vector;
    	default:
    		System.out.println("Invalid request for feature vector.");
    		return null;	
    	}
    }
    
    public HashMap<String, Double> getFeatureVector(String topic) {
    	switch (topic.toUpperCase()) {
    	case "ENTERTAINMENT":
    		return Entertainment_Feature_Vector;
    	case "HEALTH":
    		return Health_Feature_Vector;
    	case "MONEYWATCH":
    		return MoneyWatch_Feature_Vector;
    	case "POLITICS":
    		return Politics_Feature_Vector;
    	case "SPORTS":
    		return Sports_Feature_Vector;
    	case "TECH":
    		return Tech_Feature_Vector;
    	case "US":
    		return US_Feature_Vector;
    	case "WORLD":
    		return World_Feature_Vector;
    	default:
    		System.out.println("Invalid request for feature vector.");
    		return null;	
    	}
    }
    
    public double getDisposition(String topic) {
    	switch (topic.toUpperCase()) {
    	case "ENTERTAINMENT":
    		return Entertainment_Disposition;
    	case "HEALTH":
    		return Health_Disposition;
    	case "MONEYWATCH":
    		return MoneyWatch_Disposition;
    	case "POLITICS":
    		return Politics_Disposition;
    	case "SPORTS":
    		return Sports_Disposition;
    	case "TECH":
    		return Tech_Disposition;
    	case "US":
    		return US_Disposition;
    	case "WORLD":
    		return World_Disposition;
    	default:
    		System.out.println("Invalid request for disposition.");
    		return 0;	
    	}
    }
    
    // Takes original chance of getting a click, applies the emphasis
    // value function and returns the new chance of getting a click.
    public double applyEmphasisValueFunction(double score) {
    	double new_score = score + Math.min(alpha*score, beta*(1-score));
    	return new_score;
    }
}
