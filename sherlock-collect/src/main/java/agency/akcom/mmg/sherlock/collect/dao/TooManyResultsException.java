package agency.akcom.mmg.sherlock.collect.dao;

/**
 * Wrapper exception that gets thrown when Objectify get() returns too many results
 */
@SuppressWarnings("serial")
public class TooManyResultsException extends Exception {

	public TooManyResultsException() {
		super();
	}

	public TooManyResultsException(Throwable t) {
		super(t);
	}

	public TooManyResultsException(String msg) {
		super(msg);
	}

	public TooManyResultsException(String msg, Throwable t) {
		super(msg, t);
	}
}
