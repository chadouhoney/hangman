package exception;

public class UnbalancedException extends HangmanException{
	
	private int dictSize, foundWords;
	
	public UnbalancedException(int ds, int fw) {
		super();
		dictSize = ds;
		foundWords = fw;
	}
	
	@Override
	public String getMessage() {
		String msg = "At least 20% of the dictionary's words (" + (int) Math.ceil((0.2 * dictSize))
				+ ") must be of length 9 or greater\n";
		msg += "Dictionary size: "+Integer.toString(dictSize)+"\n";
		msg += "Found " + Integer.toString(foundWords) + " words of length 9 or higher: ";
		
		return msg;
	}
}
