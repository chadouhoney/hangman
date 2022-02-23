package exception;

public class UnbalancedException extends HangmanException{
	
	private int dictSize, foundWords;
	
	public UnbalancedException(int dictSize, int foundWords){
		super();
	}
	
	@Override
	public String getMessage() {
		String msg = "At least 20% of the dictionary's words must be of length 9 or greater\n";
		msg += "Dictionary size: "+Integer.toString(dictSize)+"\n";
		msg += "Number of expected words of length 9 or higher: "+Integer.toString((int)Math.ceil(0.2*dictSize))+"\n";
		msg += "Number of words of length 9 or higher found: "+Integer.toString(foundWords);
		
		return msg;
	}
}
