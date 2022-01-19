package kem.tnp.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Evgeny Kurtser on 05-Jan-22 at 6:32 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
public class LanguageDetector {
	public final static Logger log = LoggerFactory.getLogger(LanguageDetector.class);

	private static final LanguageDetector INSTANCE = new LanguageDetector();

	private Map<String, Set<String>> languageDict;

	public static LanguageDetector getLanguageDetector() {
		return INSTANCE;
	}

	/**
	 * Calculates the language code that the given input is <em>probably</em> written in.
	 *
	 * @param input input text
	 * @return the language code.
	 * @see #detectLanguages(String)
	 */
	public Optional<String> detectLanguage(String input) {
		return detectLanguages(input)
				.map(lst -> lst.get(0).getA())
				.filter(lang -> !lang.isEmpty());
	}

	/**
	 * Calculates list of language codes that the given input is <em>probably</em> written in.
	 * The result contains list of [language, a number] pairs. The number is in 0..1 range. The bigger the number, the more probable the input is written in this language.<p/>
	 * For example, if the input is mostly in English but contains some Russian words then result would be [["en", .436], ["ru", .075]]<p/>
	 * The list is sorted by probability descending.
	 *
	 * @param input input text
	 * @return list of language codes.
	 */
	public Optional<List<Tuple2<String, Float>>> detectLanguages(String input) {
		if(languageDict == null) {
			languageDict = loadLanguagesWords().orElse(Collections.emptyMap());
		}
		// Get up to 1024 words of the input text.
		final List<String> words = Stream.of(input.split("\\s+"))
				.limit(1024)
				.map(String::toLowerCase)
				.collect(Collectors.toList());
		final List<Tuple2<String, Float>> langList = languageDict.entrySet().stream()
				.map(lang -> {
					final AtomicInteger counter = new AtomicInteger();
					words.forEach(word -> {
						if(lang.getValue().contains(word)) {
							counter.getAndIncrement();
						}
					});
					float f = (float) counter.get() / lang.getValue().size();
					return new Tuple2<>(lang.getKey(), f);
				})
				.filter(tuple2 -> tuple2.getB() > 0)
				.sorted((a, b) -> Float.compare(b.getB(), a.getB()))
				.collect(Collectors.toList());
		return Optional.of(langList);
	}

	/**
	 * Loads map of languages dictionary from "dict" resource directory.
	 * Key is language code e.g "en", "it", "ru". Value is set of most common words in this language.
	 *
	 * @return map of languages dictionary or {@link Collections#emptyMap()} if "dict" resource directory cannot be accessed.
	 */
	private Optional<Map<String, Set<String>>> loadLanguagesWords() {
		// read all files from a resources folder
		log.trace("Loading dictionary files...");
		try {
			// files from /dict directory
			final List<File> dictFiles = getDictFilesFromResource();
			Map<String, Set<String>> res = dictFiles.stream()
					.collect(Collectors.toMap(
							file -> file.getName().substring(0, file.getName().lastIndexOf('.')), // map key: en.dict -> en
							fileContentFunc // map value: Set of word taken from the file (i.e. file content).
					));
			log.trace(res.size() + " dictionary files loaded.");
			return Optional.of(res);
		} catch(IOException e) {
			log.error("Could not read/access dictionary file", e);
		}
		return Optional.empty();
	}

	/**
	 * Retrieves list of files located in resource "dict" directory.
	 *
	 * @return list of files located in resource "dict" directory
	 * @throws IOException if an I/O error is thrown when accessing the starting file.
	 */
	private List<File> getDictFilesFromResource() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("dict");
		assert resource != null;
		try {
			return Files.walk(Paths.get(resource.toURI()))
					.filter(Files::isRegularFile)
					.map(Path::toFile)
					.collect(Collectors.toList());
		} catch(URISyntaxException e) {
			log.warn("It should be happen", e);
			return Collections.emptyList();
		}
	}

	private final Function<File, Set<String>> fileContentFunc = (file) -> {
		try {
			final List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			final Set<String> words = new HashSet<>(lines.size());
			words.addAll(lines);
			return words;
		} catch(IOException e) {
			log.error("Could not read/access file " + file.getName(), e);
			return Collections.emptySet();
		}
	};
}