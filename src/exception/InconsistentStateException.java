package exception;

@SuppressWarnings("serial")
public class InconsistentStateException extends EdgeSetException {
	public InconsistentStateException(String e) {
		super(e);
	}
}
