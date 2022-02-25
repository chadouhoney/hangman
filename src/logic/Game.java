package logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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
	private int wrongLetters = 0, correctLetters = 0, totalRounds = 0;
	private Double wordsLength6 = 0.0, wordsLength7_9 = 0.0, wordsLength10plus = 0.0;
	private boolean playerWin = false, playerDefeat = false;


	public Game(Dictionary dict, String target) {
		this.targetWord = target;
		this.wordsLengths = new HashMap<Integer, ArrayList<String>>();
		System.out.println("TARGET WORD: '" + target + "'");

		// initialize wordsLengths
		for (int i = 6; i <= dict.getMaxWordLength(); i++) {
			wordsLengths.put(i, new ArrayList<>());
		}

		wordsLength6 = wordsLengths.get(6).size() * 1.0 / dict.getNumOfWordsTotal();

		int wordsLength7 = dict.getMaxWordLength() >= 7 ? wordsLengths.get(7).size() : 0;
		int wordsLength8 = dict.getMaxWordLength() >= 8 ? wordsLengths.get(8).size() : 0;
		int wordsLength9 = dict.getMaxWordLength() >= 9 ? wordsLengths.get(9).size() : 0;
		wordsLength7_9 = (wordsLength7 + wordsLength8 + wordsLength9) * 1.0 / dict.getNumOfWordsTotal();

		wordsLength10plus = 0.0;
		for (int i = 10; i <= dict.getMaxWordLength(); i++) {
			wordsLength10plus += wordsLengths.get(i).size();
		}
		wordsLength10plus = wordsLength10plus / dict.getNumOfWordsTotal();

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
		totalRounds++;
		// CORRECT GUESS
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
			correctLetters++;
			if (correctLetters == targetWord.length()) {
				System.out.println("GGWP");
				playerWin = true;
				logGame("VICTORY");
			}

		}
		// WRONG GUESS
		else {
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
				playerDefeat = true;
				logGame("DEFEAT");

			}

		}
	}

	public void logGame(String victoryOrDefeat) {
		File f = new File("src/logging/history.txt");
		try {
			Scanner myReader = new Scanner(f);
			String temp = "";
			int gamesLeftToCopy = 4;
			while (myReader.hasNextLine() && gamesLeftToCopy > 0) {
				temp += (myReader.nextLine() + "\n");
				gamesLeftToCopy--;
			}
			myReader.close();
			FileWriter myWriter = new FileWriter(f.getPath());
			String currentGameStr = "Target word: " + targetWord + ", Rounds: " + totalRounds + ", Result: PLAYER "
					+ victoryOrDefeat;
			myWriter.write(currentGameStr + "\n");
			myWriter.write(temp);
			myWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getPlayerPoints() {
		return playerPoints;
	}

	public String getTargetWord() {
		return targetWord;
	}

	public boolean getPlayerWin() {
		return playerWin;
	}

	public boolean getPlayerDefeat() {
		return playerDefeat;
	}

	public HashMap<Integer, HashMap<Character, Double>> getLettersProbabilities() {
		return lettersProbabilities;
	}
}
