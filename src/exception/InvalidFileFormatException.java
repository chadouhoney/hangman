package exception;

public class InvalidFileFormatException extends HangmanException{
	
	@Override
	public String getMessage() {
		return "Error - A line in the file you asked for does not contain a word in capitals, or might have punctuation characters.";
	}
}
