package kem.tnp.common;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * This interface declares methods that convert text input e.g. "fourteen thousands two hundreds forty-six" to its numeric form -- 14246 and vise versa.
 * An implementing class should be marked with {@linkplain LanguageHandler} annotation in order to indicate the language this class works with.
 * Created by Evgeny Kurtser on 09-Jan-22 at 7:28 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
public interface LangNumberParser {
	/**
	 * Converts the given input e.g. "fourteen thousands two hundreds forty-six" to its numeric form -- 14246<p/>
	 *
	 * @param input number in text form e.g. "fourteen thousands two hundreds forty-six"
	 * @return numeric form of the given input
	 */
	Long stringToNumber(String input);

	/**
	 * Converts the given input e.g. 14246 to its text form -- "fourteen thousand two hundred and forty-six"
	 *
	 * @param input a number
	 * @return text form of the given input
	 */
	String numberToString(Long input);

	/**
	 * Configures this parser.
	 * This implementation just call {@linkplain #with(String, Object)} method for each configuration pair in this map.
	 * Unrecognized configurations should not affect this instance.
	 *
	 * @param params configuration parameters
	 * @return this instance with applied configurations
	 * @see #with(String, Object)
	 */
	default LangNumberParser with(@NotNull Map<String, Object> params) {
		params.forEach(this::with);
		return this;
	}

	/**
	 * Configures this parser.
	 * Unrecognized configuration should not affect this instance.
	 *
	 * @param key   configuration key
	 * @param value configuration value
	 * @return this instance with applied configuration
	 * @see LangNumberParser#with(Map)
	 */
	default LangNumberParser with(@NotNull String key, @NotNull Object value) {
		return this;
	}
}