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

	private int playerPoints;
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

	public int getTargetLength() {
		return targetWord.length();
	}

}
