package kem.tnp.common;


import kem.tnp.common.langparsers.LangNumberParserEmpty;
import kem.tnp.common.langparsers.LangNumberParserEn;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Evgeny Kurtser on 06-Jan-22 at 8:30 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
public abstract class StringNumberParser {

	public final static Logger log = LoggerFactory.getLogger(StringNumberParser.class);

	private static Map<String, LangNumberParser> REFLECTIONS_CACHE = null;

	/**
	 * Converts the given text input e.g. "fourteen thousands two hundreds forty-six" to its numeric form -- 14246<p/>
	 * This implementation uses {@linkplain LanguageDetector#detectLanguage(String)} method in order to detect the input language.
	 * Then it looks for class that both
	 * <ul>
	 *     <li>implements {@linkplain LangNumberParser} interface and </li>
	 *     <li>is marked with {@linkplain LanguageHandler} annotation with correspond {@link LanguageHandler#languageCode()} parameter</li>
	 * </ul>
	 * Finally, method {@linkplain LangNumberParser#stringToNumber(String)} is called.
	 *
	 * @param input number in text form e.g. "fourteen thousands two hundreds forty-six"
	 * @return numeric form of the given input, e.g. 14246
	 * @throws LanguageException          if the input language couldn't be detected
	 * @throws UnrecognizedTokenException if the given input cannot be transformed to numeric value
	 */
	public static Long textToNumber(String input, @Nullable String... defaultLanguageCode) throws LanguageException, UnrecognizedTokenException {
		return textToNumber(input, Collections.emptyMap(), defaultLanguageCode);
	}

	/**
	 * Converts the given text input e.g. "fourteen thousands two hundreds forty-six" to its numeric form -- 14246<p/>
	 * This implementation uses {@linkplain LanguageDetector#detectLanguage(String)} method in order to detect the input language.
	 * Then it looks for class that both
	 * <ul>
	 *     <li>implements {@linkplain LangNumberParser} interface and </li>
	 *     <li>is marked with {@linkplain LanguageHandler} annotation with correspond {@link LanguageHandler#languageCode()} parameter</li>
	 * </ul>
	 * Finally, method {@linkplain LangNumberParser#stringToNumber(String)} is called.
	 * Custom implementation of {@linkplain LangNumberParser} may be fine-tuned with configuration parameters.
	 *
	 * @param input  number in text form e.g. "fourteen thousands two hundreds forty-six"
	 * @param params configuration parameters. Should not be <em>null</em>
	 * @return numeric form of the given input, e.g. 14246
	 * @throws LanguageException          if the input language couldn't be detected
	 * @throws UnrecognizedTokenException if the given input cannot be transformed to numeric value
	 */
	public static Long textToNumber(String input, @NotNull Map<String, Object> params, @Nullable String... defaultLanguageCode) throws LanguageException, UnrecognizedTokenException {
		final String languageCode = LanguageDetector
				.getLanguageDetector()
				.detectLanguage(input)
				.orElseGet(() -> {
					if(defaultLanguageCode != null && defaultLanguageCode.length > 0) {
						return defaultLanguageCode[0];
					}
					throw new LanguageException("Couldn't detect language");
				});
		return getLangNumberParser(languageCode, defaultLanguageCode)
				.map(lnp -> lnp.with(params).stringToNumber(input))
				.orElseThrow(() -> new LanguageException("Couldn't find handler for language " + languageCode));
	}


	/**
	 * Converts the given input e.g. 14246 to its text form -- "fourteen thousand two hundred and forty-six".
	 * This implementation uses {@linkplain LangNumberParser} instance taken from {@linkplain #getLangNumberParser(String, String...)} and calls its {@linkplain LangNumberParser#stringToNumber(String)} method.
	 *
	 * @param input        a number
	 * @param languageCode language code (e.g. "en", "fr", "it", "ru" etc)
	 * @return text form of the given input
	 */
	public static String numberToString(@NotNull Long input, @NotNull String languageCode) throws LanguageException, UnrecognizedTokenException {
		return numberToString(input, languageCode, Collections.emptyMap());
	}

	/**
	 * Converts the given input e.g. 14246 to its text form -- "fourteen thousand two hundred and forty-six".
	 * This implementation uses {@linkplain LangNumberParser} instance taken from {@linkplain #getLangNumberParser(String, String...)} and calls its {@linkplain LangNumberParser#stringToNumber(String)} method.
	 * Custom implementation of {@linkplain LangNumberParser} may be fine-tuned with configuration parameters.
	 * For example, {@linkplain LangNumberParserEn#with(String, Object)} recognizes ["onePrefix"->"a/one"] configuration.
	 *
	 * @param input        a number
	 * @param languageCode language code (e.g. "en", "fr", "it", "ru" etc)
	 * @param params       configuration parameters. Should not be <em>null</em>
	 * @return text form of the given input
	 * @see LangNumberParserEn#with(String, Object)
	 */
	public static String numberToString(@NotNull Long input, @NotNull String languageCode, @NotNull Map<String, Object> params) throws LanguageException, UnrecognizedTokenException {
		return getLangNumberParser(languageCode)
				.map(lnp -> lnp.with(params).numberToString(input))
				.orElseThrow(() -> new LanguageException("Couldn't find handler for language " + languageCode));
	}


	/**
	 * Retrieves LangNumberParser for the given language code.
	 *
	 * @param languageCode language code (e.g. "en", "fr", "it", "ru" etc)
	 * @return LangNumberParser for the given language code. If no LangNumberParser found then {@linkplain LangNumberParserEmpty#getInstance()} is returned.
	 */
	public static Optional<LangNumberParser> getLangNumberParser(@NotNull String languageCode, @Nullable String... defaultLanguageCode) {
		if(REFLECTIONS_CACHE == null) {
			REFLECTIONS_CACHE = loadClassCache();
		}
		final LangNumberParser langNumberParser = REFLECTIONS_CACHE.get(languageCode);
		if(langNumberParser == null && defaultLanguageCode != null && defaultLanguageCode.length > 0) {
			return Optional.ofNullable(REFLECTIONS_CACHE.get(defaultLanguageCode[0]));
		}
		return Optional.ofNullable(langNumberParser);
	}

	private static Map<String, LangNumberParser> loadClassCache() {
		Reflections reflections = new Reflections(Scanners.SubTypes, Scanners.TypesAnnotated);
		final Set<Class<? extends LangNumberParser>> subTypesOf = reflections.getSubTypesOf(LangNumberParser.class);
		final Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(LanguageHandler.class);
		typesAnnotatedWith.retainAll(subTypesOf);
		return typesAnnotatedWith
				.stream()
				.collect(Collectors.toMap(
						cc -> cc.getAnnotation(LanguageHandler.class).languageCode(),
						cc -> {
							try {
								return (LangNumberParser) cc.newInstance();
							} catch(InstantiationException | IllegalAccessException e) {
								log.warn("Could not create new instance of " + cc.getName(), e);
								return LangNumberParserEmpty.getInstance();
							}
						}));
	}
}