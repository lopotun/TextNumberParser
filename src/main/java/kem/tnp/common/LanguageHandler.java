package kem.tnp.common;

import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicated the language that methods of this class use in order to handle text to number conversion and vise versa
 * Created by Evgeny Kurtser on 10-Jan-22 at 9:11 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LanguageHandler {
	/**
	 * Supported language code e.g. "en" for English, "ru" for Russian, "he" for Hebrew etc.
	 *
	 * @return supported language
	 */
	@Pattern(regexp = "\\w{2}")
	String languageCode();
}