package sample;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

	private static HashMap<Character, Double> alphabet = new HashMap<Character, Double>() {
		{
			for (Character i = 'A'; i <= 'Z'; i++) {
				this.put(i, 0.0);
			}
		}
	};

	private int playerPoints = 0;
	private String targetWord;
	private HashMap<Integer, ArrayList<String>> wordsLengths;
	private HashMap<Integer, HashMap<Character, Double>> lettersProbabilities;
	private int wrongLetters = 0;


	public Game(Dictionary dict, String target) {
		// this.dictionary = dict;
		this.targetWord = target;
		this.wordsLengths = new HashMap<Integer, ArrayList<String>>();
		System.out.println("TARGET WORD: '" + target + "'");

		// initialize wordsLengths
		for (int i = 6; i <= dict.getMaxWordLength(); i++) {
			wordsLengths.put(i, new ArrayList<>());
		}

		// fill wordsLengths
		for (String word : dict.getWords()) {
			wordsLengths.get(word.length()).add(word);
		}
		System.out.println(wordsLengths);

		// initialization and initial computation of lettersProbabilities
		int wordsWithLengthEqualToTarget = wordsLengths.get(target.length()).size();
		lettersProbabilities = new HashMap<Integer, HashMap<Character, Double>>();
		for (int i = 0; i < target.length(); i++) {
			HashMap<Character, Double> m = (HashMap<Character, Double>) alphabet.clone();
			for (String word : wordsLengths.get(target.length())) {
				m.put(word.charAt(i), m.get(word.charAt(i)) + 1.0);
			}
			for (Character c = 'A'; c <= 'Z'; c++) {
				m.put(c, m.get(c) * 1.0 / wordsWithLengthEqualToTarget);
			}
			System.out.println(m);
			lettersProbabilities.put(i, m);
		}
		System.out.println(lettersProbabilities);

	}

	public void guess(Integer i, Character c) {
		if (targetWord.charAt(i) == c) {
			// remove words that do not contain c in index i
			for (String word : wordsLengths.get(targetWord.length())) {
				if (Character.valueOf(word.charAt(i)) != c) {
					wordsLengths.get(targetWord.length()).remove(word);
				}
			}

			// award points to player
			Double letterProb = lettersProbabilities.get(i).get(Character.valueOf(c));
			if (letterProb >= 0.6) {
				playerPoints += 5;
			} else if (letterProb >= 0.4) {
				playerPoints += 10;
			} else if (letterProb >= 0.25) {
				playerPoints += 15;
			} else {
				playerPoints += 30;
			}

			// recalculate probabilities of letters
			int wordsWithLengthEqualToTarget = wordsLengths.get(targetWord.length()).size();
			for (int j = 0; j < targetWord.length(); j++) {
				HashMap<Character, Double> m = (HashMap<Character, Double>) alphabet.clone();
				for (String word : wordsLengths.get(targetWord.length())) {
					m.put(word.charAt(i), m.get(word.charAt(i)) + 1.0);
				}
				for (Character cc = 'A'; c <= 'Z'; cc++) {
					m.put(cc, m.get(cc) * 1.0 / wordsWithLengthEqualToTarget);
				}
				System.out.println(m);
				lettersProbabilities.replace(i, m);
			}

			// check for victory
			// todo

		} else {
			// remove words that do not contain c in index i
			for (String word : wordsLengths.get(targetWord.length())) {
				if (Character.valueOf(word.charAt(i)) == c) {
					wordsLengths.get(targetWord.length()).remove(word);
				}
			}
			// subtract points from player (points cannot go below zero)
			playerPoints = Math.max(playerPoints - 15, 0);

			// recalculate probabilities of letters
			int wordsWithLengthEqualToTarget = wordsLengths.get(targetWord.length()).size();
			for (int j = 0; j < targetWord.length(); j++) {
				HashMap<Character, Double> m = (HashMap<Character, Double>) alphabet.clone();
				for (String word : wordsLengths.get(targetWord.length())) {
					m.put(word.charAt(i), m.get(word.charAt(i)) + 1.0);
				}
				for (Character cc = 'A'; c <= 'Z'; cc++) {
					m.put(cc, m.get(cc) * 1.0 / wordsWithLengthEqualToTarget);
				}
				System.out.println(m);
				lettersProbabilities.replace(i, m);
			}

			// increase wrong attempts and check for defeat
			wrongLetters++;

			if (wrongLetters >= 6) {
				System.out.println("YOU LOSE, GIT GUD NOOB");
			}

		}

	}
}
