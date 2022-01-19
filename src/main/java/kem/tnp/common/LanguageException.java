package kem.tnp.common;

/**
 * Created by Evgeny Kurtser on 10-Jan-22 at 7:40 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
public class LanguageException extends RuntimeException {
	public LanguageException(String message) {
		super(message);
	}

	@SuppressWarnings("unused")
	public LanguageException(String message, Throwable cause) {
		super(message, cause);
	}
}
