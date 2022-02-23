package exception;

public class URLException extends HangmanException{
	
	@Override
	public String getMessage() {
		return "Could not retrieve a dictionary. Either the ID you entered is not valid, or there was a problem getting the JSON object.";
	}
}
