package kem.tnp.langparsers.common;

import kem.tnp.common.LanguageException;
import kem.tnp.common.StringNumberParser;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Evgeny Kurtser on 08-Jan-22 at 11:43 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
class StringNumberParserTest {

	@Test
	void parse1() {
		assertEquals(StringNumberParser.textToNumber("one"), 1);
		assertEquals(StringNumberParser.numberToString(1L, "en"), "one");
		assertEquals(StringNumberParser.textToNumber("один", "ru"), 1);
		assertEquals(StringNumberParser.numberToString(1L, "ru"), "один");
	}

	@Test
	void parse10() {
		assertEquals(StringNumberParser.textToNumber("ten"), 10);
		assertEquals(StringNumberParser.textToNumber("десять", "ru"), 10);
	}

	@Test
	void parse15() {
		assertEquals(StringNumberParser.textToNumber("fifteen"), 15);
		assertEquals(StringNumberParser.textToNumber("пятнадцать"), 15);
		assertThrows(LanguageException.class, () -> StringNumberParser.textToNumber("п'ятнадцять"), "Couldn't find handler for language ua");
		assertThrows(LanguageException.class, () -> StringNumberParser.textToNumber("quindici"), "Couldn't find handler for language it");
	}

	@Test
	void parse40() {
		assertEquals(StringNumberParser.textToNumber("forty"), 40);
		assertEquals(StringNumberParser.textToNumber("сорок", "ru"), 40);
	}

	@Test
	void parse32() {
		assertEquals(StringNumberParser.textToNumber("thirty two"), 32);
		assertEquals(StringNumberParser.textToNumber("тридцать два"), 32);
	}

	@Test
	void parse58() {
		assertEquals(StringNumberParser.textToNumber("fifty eight"), 58);
		assertEquals(StringNumberParser.textToNumber("пятьдесят восемь"), 58);
		assertThrows(LanguageException.class, () -> StringNumberParser.textToNumber("п'ятдесят вісім"), "Couldn't find handler for language ua");
	}

	@Test
	void parse100() {
		assertEquals(StringNumberParser.textToNumber("a Hundred"), 100);
		assertEquals(StringNumberParser.textToNumber("сто"), 100);
	}

	@Test
	void parse107() {
		assertEquals(StringNumberParser.textToNumber("a hundred and seven"), 107);
		assertEquals(StringNumberParser.textToNumber("one hundred and seven"), 107);
		assertEquals(StringNumberParser.textToNumber("сто семь"), 107);
	}

	@Test
	void parse118() {
		assertEquals(StringNumberParser.textToNumber("a hundred eighteen"), 118);
		assertEquals(StringNumberParser.textToNumber("сто восемнадцать"), 118);
	}

	@Test
	void parse5_000() {
		assertEquals(StringNumberParser.textToNumber("five thousand"), 5_000);
		assertEquals(StringNumberParser.textToNumber("пять тысяч"), 5_000);
	}

	@Test
	void parse5_274() {
		assertEquals(StringNumberParser.textToNumber("five thousand two hundred seventy four"), 5_274);
		assertEquals(StringNumberParser.textToNumber("five thousand two hundred seventy-four"), 5_274);
		assertEquals(StringNumberParser.textToNumber("пять тысяч двести семьдесят четыре"), 5_274);
	}


	@Test
	void parse152_385_112_008() {
		assertEquals(152_385_112_008L, StringNumberParser.textToNumber("a hundred and fifty two billion three hundred eighty five million a hundred twelve thousand and eight"));
		assertEquals(152_385_112_008L, StringNumberParser.textToNumber("сто пятьдесят два миллиарда триста восемьдесят пять миллионов сто двенадцать тысяч восемь"));

		Long numEn = StringNumberParser.textToNumber("a hundred and fifty two billion three hundred eighty five million a hundred twelve thousand and eight");
		Long numRu = StringNumberParser.textToNumber("сто пятьдесят два миллиарда триста восемьдесят пять миллионов сто двенадцать тысяч восемь");
		assert numEn == 152_385_112_008L;
		assert numEn.equals(numRu);

		String textEn = "one hundred fifty-two billion three hundred eighty-five million one hundred twelve thousand and eight";
		String textRu = "сто пятьдесят два миллиарда триста восемьдесят пять миллионов сто двенадцать тысяч восемь";

		assert textEn.equals(StringNumberParser.numberToString(152_385_112_008L, "en"));
		assert textRu.equals(StringNumberParser.numberToString(152_385_112_008L, "ru"));
	}

	@Test
	void parseEx1() {
		final String textEn = "one hundred fifty-two billion three hundred eighty-five million one hundred twelve thousand and eight";
		final String textRu = "сто пятьдесят два миллиарда триста восемьдесят пять миллионов сто двенадцать тысяч восемь";
		final Long num = 152_385_112_008L;

		// Convert English text to a number.
		final Long numEn = StringNumberParser.textToNumber(textEn);
		assert numEn.equals(num);
		// Convert Russian text to a number.
		final Long numRu = StringNumberParser.textToNumber(textRu);
		assert numRu.equals(num);

		// Convert a number to English text.
		assert textEn.equals(StringNumberParser.numberToString(num, "en"));
		// Convert a number to Russian text.
		assert textRu.equals(StringNumberParser.numberToString(num, "ru"));
	}

	@Test
	void parseEx2() {
		final Long num = 152L;
		final Map<String, Object> params = new HashMap<>();

		// A parser may support configuration parameters.
		// English parser, for example, supports two parameters: "onePrefix" and "useHyphen".

		params.put("onePrefix", "one"); // use "one" prefix for a hundred: one hundred (default)
		params.put("useHyphen", "false"); // do not use hyphen: fifty two
		assert "one hundred fifty two".equals(StringNumberParser.numberToString(num, "en", params));

		params.put("onePrefix", "one"); // use "one" prefix for a hundred: one hundred (default)
		params.put("useHyphen", "true"); // use hyphen: fifty-two (default)
		assert "one hundred fifty-two".equals(StringNumberParser.numberToString(num, "en", params));

		params.put("onePrefix", "a"); // use "a" prefix for a hundred: a hundred
		params.put("useHyphen", "false"); // do not use hyphen: fifty two
		assert "a hundred fifty two".equals(StringNumberParser.numberToString(num, "en", params));

		params.put("onePrefix", "a"); // use "a" prefix for a hundred: a hundred
		params.put("useHyphen", "true"); // use hyphen: fifty-two (default)
		assert "a hundred fifty-two".equals(StringNumberParser.numberToString(num, "en", params));
	}

	@Test
	void parseStringNumberInvalidInput() {
		assertThrows(RuntimeException.class, () -> StringNumberParser.textToNumber("five thousands two hundreds zaza seventy four"));
		assertThrows(RuntimeException.class, () -> StringNumberParser.textToNumber("пять тысяч двести zaza семьдесят четыре"));
	}
}