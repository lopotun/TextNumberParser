package kem.tnp.common;

/**
 * Created by Evgeny Kurtser on 10-Jan-22 at 8:40 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
public class UnrecognizedTokenException extends RuntimeException {
	public UnrecognizedTokenException(String message) {
		super(message);
	}

	@SuppressWarnings("unused")
	public UnrecognizedTokenException(String message, Throwable cause) {
		super(message, cause);
	}
}
