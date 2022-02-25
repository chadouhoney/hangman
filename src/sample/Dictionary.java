package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import exception.*;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


public class Dictionary {
	private ArrayList<String> words;
	private int numOfWordsTotal=0;
	private int numOfWordsAtLeastNineLong=0;

	public int getNumOfWordsTotal() {
		return numOfWordsTotal;
	}

	public int getNumOfWordsAtLeastNineLong() {
		return numOfWordsAtLeastNineLong;
	}

	public int getMaxWordLength() {
		return maxWordLength;
	}

	private int maxWordLength = 0;
	
	private static JsonObject getJson(String openLibraryId) throws IOException {
		URL url = new URL("https://openlibrary.org/works/"+openLibraryId+".json");
		
		//make connection
		URLConnection urlc = url.openConnection();
		urlc.setDoOutput(false);
		urlc.setAllowUserInteraction(false);
		
		JsonReader rdr = Json.createReader(urlc.getInputStream());
		JsonObject obj = rdr.readObject();
		return obj;
	}
	
	public ArrayList<String> getWords(){
		return words;
	}
	
	private ArrayList<String> createWordsList(String[] s){
		ArrayList ret = new ArrayList<>();
		for(String word : s){
			// avoid InvalidRangeException and InvalidCountException. Always make sure that when a dictionary is created,
			// the words will occur once, and will be at least 6 letters long
			if(word.length() > 5 && !ret.contains(word)){
				ret.add(word);
				numOfWordsTotal++;
				if(word.length()>8){
					numOfWordsAtLeastNineLong++;
				}
				if (word.length() > maxWordLength) {
					maxWordLength = word.length();
				}
			}
		}
		return ret;
	}
	
	public static void createDictionaryTxtFile(String[] words, String openLibraryId) {

	}

	public Dictionary(String openLibraryId) throws HangmanException {
		if (openLibraryId.isEmpty()) {
			throw new HangmanException() {
				@Override
				public String getMessage() {
					return "ID cannot be empty!!!";
				}
			};
		}
		else {
			// try to find a txt file with the given id
			System.out.println("Trying to find a txt file");
			File f = new File("src/medialab/hangman_" + openLibraryId + ".txt");
			if (f.exists()) {
				System.out.println("found the txt file");
				// create dictionary using the file
				try {
					words = new ArrayList<>();
					Scanner myReader = new Scanner(f);
					while (myReader.hasNextLine()) {
						String word = myReader.nextLine();
						if (isInvalid(word)) {
							throw new InvalidFileFormatException();
						}
						if (word.length() > 5 && !words.contains(word)) {
							words.add(word);
							numOfWordsTotal++;
							if (word.length() > 8) {
								numOfWordsAtLeastNineLong++;
							}
							if (word.length() > maxWordLength) {
								maxWordLength = word.length();
							}
						}

					}

					if (numOfWordsTotal < 20) {
						throw new UndersizeException();
					}

					if (numOfWordsAtLeastNineLong < 0.2 * numOfWordsTotal) {
						throw new UnbalancedException(numOfWordsTotal, numOfWordsAtLeastNineLong);
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			// if such a file does not exist, make an api call to fetch the data, save the
			// dictionary to a txt file and return it.
			else {
				System.out.println("Didnt find the txt file. Trying to fetch the data from the API");
				JsonObject obj = null;
				try {
					obj = getJson(openLibraryId);

					// get the string of the description
					String description = obj.get("description").asJsonObject().get("value").toString();

					// remove punctuation
					// https://stackoverflow.com/questions/18830813/how-can-i-remove-punctuation-from-input-text-in-java
					description = description.replaceAll("\\p{Punct}", "");

					// Turn all letters to capitals
					description = description.toUpperCase();

					// System.out.println(description);
					words = createWordsList(description.split(" "));

					System.out.println("Total words: " + Integer.toString(numOfWordsTotal));
					System.out.println(
							"Total words longer than 9 characters: " + Integer.toString(numOfWordsAtLeastNineLong) + "("
									+ Double.toString(numOfWordsAtLeastNineLong * 1.0 / numOfWordsTotal) + "%)");

					if (numOfWordsTotal < 20) {
						throw new UndersizeException();
					}

					if (numOfWordsAtLeastNineLong < 0.2 * numOfWordsTotal) {
						throw new UnbalancedException(numOfWordsTotal, numOfWordsAtLeastNineLong);
					}

					// save the dict to a new file
					try {
						System.out.println("Trying to create the file");
						f.createNewFile();
						FileWriter myWriter = new FileWriter(f.getPath());
						for (String word : words) {
							System.out.println("Writing word '" + word + "' to ");
							myWriter.write(word + "\n");
						}
						myWriter.close();
						System.out.println("Successfully wrote to the file.");
					} catch (IOException e) {
						e.printStackTrace();
					}

				} catch (IOException e) {
					throw new URLException();
				}
			}
		}
	}

	private boolean isInvalid(String word) {
		// todo
		return false;
	}
}
