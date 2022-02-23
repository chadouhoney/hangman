package sample;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

	private int playerPoints;
	private String targetWord;
	private HashMap<Integer, ArrayList<String>> wordsLengths;
	private int wrongLetters = 0;

	public Game(Dictionary dict, String target) {
		// this.dictionary = dict;
		this.targetWord = target;
		this.wordsLengths = new HashMap<Integer, ArrayList<String>>();

		// initialize wordsLengths
		for (int i = 6; i <= dict.getMaxWordLength(); i++) {
			wordsLengths.put(i, new ArrayList<>());
		}

		for (String word : dict.getWords()) {
			wordsLengths.get(word.length()).add(word);
		}

		System.out.println(wordsLengths);

	}

	public int getTargetLength() {
		return targetWord.length();
	}

}
