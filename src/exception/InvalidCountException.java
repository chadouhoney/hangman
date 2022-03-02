package exception;

public class InvalidCountException extends HangmanException {

	@Override
	public String getMessage() {
		return "The dictionary must not contain duplicate words";
	}
}
