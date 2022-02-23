package exception;

public class UndersizeException extends HangmanException{
	@Override
	public String getMessage() {
		return "The dictionary must contain at least 20 words";
	}
}
