package sample;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import exception.HangmanException;
import exception.UnbalancedException;
import exception.UndersizeException;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


public class Dictionary {
	private ArrayList<String> words;
	private int numOfWordsTotal=0;
	private int numOfWordsAtLeastNineLong=0;
	
	private JsonObject getJson(String openLibraryId) throws IOException {
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
			}
		}
		return ret;
	}
	
	public Dictionary(String openLibraryId) throws HangmanException {
		JsonObject obj = null;
		try {
			obj = getJson(openLibraryId);
			
			//get the string of the description
			String description = obj.get("description").asJsonObject().get("value").toString();
			
			//remove punctuation https://stackoverflow.com/questions/18830813/how-can-i-remove-punctuation-from-input-text-in-java
			description=description.replaceAll("\\p{Punct}","");
			
			//Turn all letters to capitals
			description=description.toUpperCase();
			
			System.out.println(description);
			words = createWordsList(description.split(" "));
			
			System.out.println("Total words: "+Integer.toString(numOfWordsTotal));
			System.out.println("Total words longer than 9 characters: "+Integer.toString(numOfWordsAtLeastNineLong));
			
			if(numOfWordsTotal < 20 ){
				throw new UndersizeException();
			}
			
			if(numOfWordsAtLeastNineLong < 0.2*numOfWordsTotal){
				throw new UnbalancedException(numOfWordsTotal,numOfWordsAtLeastNineLong);
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
