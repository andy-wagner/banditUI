package com.banditUI.articleViewer.system;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;


import edu.stanford.nlp.util.ArrayUtils;

public class Exp4Simulator {

	private Person person;
	private int num_experts;
	private int num_display_spaces;
	private Expert[] experts;
	private ArticleSource articleSource;
	private double learning_rate;
	private double[] w;

	// ** state **
	private HashMap<Expert, HashMap<Article, Double>> currentAdvice;
	private HashMap<Article, Integer> currentDisplay;
	
	
	public Exp4Simulator(Person p, int num_exps, ArticleSource source, int num_spaces,
			double learn_rate) {
		person = p;
		num_experts = num_exps;
		experts = new Expert[num_experts];
		w = new double[num_experts];
		// Include uniform expert, as specified by Auer 2001
		experts[0] = new UniformExpert();
		w[0] = 1;
		for (int i = 1; i < num_experts; i++) {
			experts[i] = new Expert();
			w[i] = 1; 
		}
		articleSource = source;
		num_display_spaces = num_spaces;
		learning_rate = learn_rate;
	}

	public HashMap<Article, Integer> getCurrentDisplay() {
		return currentDisplay;
	}
	
	public void runSimulationOneDay(Calendar date) {
		int index = 0;
		double Wt, sum, r;
		Article articleToDisplay;
		
		//articles available for the day.
		Article[] articles = articleSource.getArticlesForDate(date);
		int num_articles = articles.length;
		double[] probabilities = new double[num_articles];
		
		HashMap<Article, Double> totalledAdvice;
		
		// HashMap mapping articles to how many times they've been displayed.
		// Initially, all articles have of course been displayed 0 times.
		HashMap<Article, Integer> articlesDisplayed = new HashMap<Article, Integer>();
		for (int i = 0; i < num_articles; i++)
			articlesDisplayed.put(articles[i], 0);
		
		// This keeps track of, over each of the 8 square recommendations,
		// how much each expert recommended each article total. This is
		// required for updating weights after getting rewards.
		HashMap<Expert, HashMap<Article, Double>> totalledExpertAdvice = 
				new HashMap<Expert, HashMap<Article, Double>>();
		
		for (int i = 0; i < num_experts; i++) {
			totalledAdvice = new HashMap<Article,Double>();
			for (Article a : articles)
				totalledAdvice.put(a, 0.0);
			totalledExpertAdvice.put(experts[i], totalledAdvice);
		}
		
		
		// For each space we have available...
		for (int space = 0; space < num_display_spaces; space++) {
			// Steps as specified by Auer et al in
			// "The Nonstochastic Multiarmed Bandit Problem"
			
			// Step 1 : get advice vectors from all experts.
			HashMap<Expert, HashMap<Article, Double>> expertAdvice = new HashMap<Expert, HashMap<Article, Double>>();
			getAllExpertsAdvice(articlesDisplayed, expertAdvice);
			for (int i = 0; i < num_experts; i++) {
				for (Expert e : totalledExpertAdvice.keySet()) {
					totalledAdvice = totalledExpertAdvice.get(e);
					for (Article a : totalledAdvice.keySet()) {
						totalledAdvice.put(a, 
							totalledAdvice.get(a) + expertAdvice.get(e).get(a));
					}
				}
			}
			
			// Step 2 : Get probability distribution over articles.
			Wt = 0;
			for (int i = 0; i < num_experts; i++) {
				Wt += w[i];
			}
			for (int i = 0; i < num_articles; i++) {
				sum = 0;
				for (int j = 0; j < num_experts; j++) {
					sum += w[j]*expertAdvice.get(experts[j]).get(articles[i]) / Wt;
				}
				probabilities[i] = (1 - learning_rate) * sum * (learning_rate/num_articles);
			}
			
			// Step 3 : Choose an article to display from this distribution.
			// First, set up probabilities array for easy sampling
			double total = 0;
			for (int i = 0; i < probabilities.length; i++)
				total += probabilities[i];
			for (int i = 0; i < probabilities.length; i++)
				probabilities[i] /= total;
			for (int i = 1; i < num_articles; i++)
				probabilities[i] = probabilities[i] + probabilities[i-1];
			probabilities[num_articles - 1] = 1;
			r = Math.random();
			for (int i = 0; i < num_articles; i++) {
				if (r < probabilities[i]) {
					index = i;
					break;
				}
			}
			articleToDisplay = articles[index];
			articlesDisplayed.put(articleToDisplay, 
					articlesDisplayed.get(articleToDisplay)+1);
		}
		// Save current state for decoupling
		currentAdvice = totalledExpertAdvice;
		currentDisplay = articlesDisplayed;
	}

	public void reward(HashMap<Article,Integer> display, HashMap<Article, Boolean> clicks) {
		// TODO: update the bandit according to the clicks (rewards)!
	}
	
	private void getAllExpertsAdvice(HashMap<Article, Integer> articles, 
			HashMap<Expert, HashMap<Article, Double>> expertAdvice) {
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		ReentrantLock l = new ReentrantLock();
		for (int i = 0; i < num_experts; i++) {
			threads.add(new Thread(new ExpertAdviceGetter(experts[i], articles, expertAdvice, l)));
		}
		for (int i = 0; i < threads.size(); i++)
			threads.get(i).start();
		for (int i = 0; i < threads.size(); i++)
		{
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {}
		}

	}
		
	
}