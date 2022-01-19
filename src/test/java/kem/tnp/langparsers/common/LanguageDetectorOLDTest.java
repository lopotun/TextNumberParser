package kem.tnp.langparsers.common;

import kem.tnp.common.LanguageDetectorOLD;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * Created by Evgeny Kurtser on 11-Jan-22 at 11:20 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
class LanguageDetectorOLDTest {

	@Test
	void detectHE() {
		final Set<LanguageDetectorOLD.Language> lang = LanguageDetectorOLD.detectLanguage("אימא נקתה חלון");
		assertIterableEquals(lang, Collections.singleton(LanguageDetectorOLD.Language.HEBREW));
	}

	@Test
	void detectRU() {
		final Set<LanguageDetectorOLD.Language> lang = LanguageDetectorOLD.detectLanguage("Мама мыла раму");
		assertIterableEquals(lang, Collections.singleton(LanguageDetectorOLD.Language.RUSSIAN));
	}

	@Test
	void detectEN() {
		final Set<LanguageDetectorOLD.Language> lang = LanguageDetectorOLD.detectLanguage("Mom cleaned a window");
		assertIterableEquals(lang, Collections.singleton(LanguageDetectorOLD.Language.ENGLISH));
	}

	@Test
	void detectMixed() {
		final Set<LanguageDetectorOLD.Language> lang = LanguageDetectorOLD.detectLanguage("Mom мыла חלון");
		assertEquals(lang.size(), 3);
	}
}