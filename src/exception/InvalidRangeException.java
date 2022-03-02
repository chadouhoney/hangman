package exception;

public class InvalidRangeException extends HangmanException {

	@Override
	public String getMessage() {
		return "The dictionary must not contain words with 5 letters or less";
	}
}
