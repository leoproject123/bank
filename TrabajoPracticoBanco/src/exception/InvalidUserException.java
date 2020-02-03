package exception;

public class InvalidUserException extends Exception {

	public InvalidUserException() {
		super();
	}

	public InvalidUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUserException(String message) {
		super(message);
	}

	public InvalidUserException(Throwable cause) {
		super(cause);
	}

}
