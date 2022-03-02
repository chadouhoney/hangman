package logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import ui.Guess;

public class Game {

	private static HashMap<Character, Double> alphabet = new HashMap<Character, Double>() {
		{
			for (Character i = 'A'; i <= 'Z'; i++) {
				this.put(i, 0.0);
			}
		}
	};

	private String targetWord;
	private HashMap<Integer, ArrayList<String>> wordsLengths;
	private HashMap<Integer, HashMap<Character, Double>> lettersProbabilities;
	private int wrongLetters, correctLetters, totalRounds, playerPoints;
	private Double wordsLength6 = 0.0, wordsLength7_9 = 0.0, wordsLength10plus = 0.0;
	private boolean playerWin = false, playerDefeat = false;
	private Dictionary dictionary;

	/**
	 * Given the current dictionary, chooses a new random word and creates a new
	 * game, with the given word for a target.
	 *
	 * @return the new game
	 */
	public Game newGame() {
		Random rand = new Random();
		return new Game(dictionary, dictionary.getWords().get(rand.nextInt(dictionary.getWords().size())));
	}

	/**
	 * Instantiates a new Game, given a dictionary and a target word.
	 *
	 * @param dict
	 *            the dictionary
	 * @param target
	 *            the target word
	 */
	public Game(Dictionary dict, String target) {
		this.dictionary = dict;
		this.targetWord = target;
		this.wordsLengths = new HashMap<Integer, ArrayList<String>>();
		this.totalRounds = 0;
		this.playerPoints = 0;
		this.correctLetters = 0;
		this.wrongLetters = 0;
		System.out.println("TARGET WORD: '" + target + "'");

		// initialize wordsLengths
		for (int i = 6; i <= dict.getMaxWordLength(); i++) {
			wordsLengths.put(i, new ArrayList<>());
		}

		// fill wordsLengths
		for (String word : dict.getWords()) {
			wordsLengths.get(word.length()).add(word);
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
			lettersProbabilities.put(i, m);
		}

		System.out.println("Candidate words:");
		System.out.println(wordsLengths.get(targetWord.length()));
		System.out.println("Probabilities:");
		System.out.println(lettersProbabilities);

	}

	/**
	 * Given the input Guess object, checks if the letter chosen by the Guess object
	 * fits at the index chosen by the object If the letter is correct, recalculates
	 * the letters' probabilities after removing all words that do not contain the
	 * certain letter at the certain index.
	 *
	 * If the letter is incorrect, recalculates the letters' probabilities after
	 * removing all words that contain the certain letter at the certain index.
	 * 
	 * After each case, points are awarded/subtracted from the player and checks are
	 * made for victory/defeat, and if the criteria are met, the game is logged at
	 * "src/logging/history.txt"
	 * 
	 *
	 * @param move
	 *            a Guess object. Contains information about the index and the
	 *            letter.
	 * @return true if the guess is successful, false otherwise
	 */
	public boolean guess(Guess move) {
		int guessIndex = move.getForPosition();
		Character guessCharacter = move.getStringProperty().charAt(0);
		totalRounds++;

		// CORRECT GUESS
		if (targetWord.charAt(guessIndex) == guessCharacter) {
			// remove words that do not contain c in index i
			wordsLengths.get(targetWord.length())
					.removeIf(word -> Character.valueOf(word.charAt(guessIndex)) != guessCharacter);

			// award points to player
			Double letterProb = lettersProbabilities.get(guessIndex).get(Character.valueOf(guessCharacter));
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
					m.put(word.charAt(j), m.get(word.charAt(j)) + 1.0);
				}
				for (Character cc = 'A'; cc <= 'Z'; cc++) {
					m.put(cc, m.get(cc) * 1.0 / wordsWithLengthEqualToTarget);
				}
				lettersProbabilities.replace(j, m);
			}

			// check for victory
			correctLetters++;
			if (correctLetters == targetWord.length()) {
				System.out.println("GGWP");
				playerWin = true;
				move.playerWon.set(true);
				move.setPlayerWin(true);
				logGame("VICTORY");
			}
			return true;
		}
		// WRONG GUESS
		else {
			// remove words that do not contain c in index i
			wordsLengths.get(targetWord.length())
					.removeIf(word -> Character.valueOf(word.charAt(guessIndex)) == guessCharacter);
			// subtract points from player (points cannot go below zero)
			playerPoints = Math.max(playerPoints - 15, 0);

			// recalculate probabilities of letters
			int wordsWithLengthEqualToTarget = wordsLengths.get(targetWord.length()).size();
			for (int j = 0; j < targetWord.length(); j++) {
				HashMap<Character, Double> m = (HashMap<Character, Double>) alphabet.clone();
				for (String word : wordsLengths.get(targetWord.length())) {
					m.put(word.charAt(j), m.get(word.charAt(j)) + 1.0);
				}
				for (Character cc = 'A'; cc <= 'Z'; cc++) {
					m.put(cc, m.get(cc) * 1.0 / wordsWithLengthEqualToTarget);
				}
				lettersProbabilities.replace(j, m);
			}

			// increase wrong attempts and check for defeat
			wrongLetters++;
			if (wrongLetters >= 6) {
				System.out.println("YOU LOSE, GIT GUD NOOB");
				playerDefeat = true;
				move.playerLost.set(true);
				move.setPlayerLose(true);
				logGame("DEFEAT");

			}
			return false;
		}
	}

	/**
	 * Store the game's results at "src/logging/history.txt" (target word, rounds,
	 * outcome)
	 *
	 * @param victoryOrDefeat
	 *            the game's outcome
	 */
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

	/**
	 * Gets player points.
	 *
	 * @return the player's points
	 */
	public int getPlayerPoints() {
		return playerPoints;
	}

	/**
	 * Gets target word.
	 *
	 * @return the target word
	 */
	public String getTargetWord() {
		return targetWord;
	}

	/**
	 * Gets letters probabilities.
	 *
	 * @return the letters probabilities
	 */
	public HashMap<Integer, HashMap<Character, Double>> getLettersProbabilities() {
		return lettersProbabilities;
	}

	/**
	 * Returns a hashmap. The hashmap's keys are integers that represent the length
	 * of the words contained in the correspinding values' Arraylists. *
	 * 
	 * @return the wordsLengths
	 */
	public HashMap<Integer, ArrayList<String>> getWordsLengths() {
		return wordsLengths;
	}

	/**
	 * 
	 *
	 * @return the percentage of successful guesses
	 */
	public double getSussessfulPercentage() {
		return (correctLetters * 1.0 / totalRounds);
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public Double getWordsLength6() {
		return wordsLength6;
	}

	public Double getWordsLength7_9() {
		return wordsLength7_9;
	}

	public Double getWordsLength10plus() {
		return wordsLength10plus;
	}
}
