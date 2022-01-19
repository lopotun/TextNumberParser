package kem.tnp.langparsers;

import kem.tnp.common.LangNumberParser;
import kem.tnp.common.StringNumberParser;
import kem.tnp.common.langparsers.LangNumberParserEn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Evgeny Kurtser on 08-Jan-22 at 11:43 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
class LangNumberParserEnTest {

	@Test
	void numberToString314_042_517_234_583() {
		long number = 314_042_517_234_583L;
		String text = "three hundred fourteen trillion forty-two billion five hundred seventeen million two hundred thirty-four thousand five hundred eighty-three";
		LangNumberParserEn parser = new LangNumberParserEn();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void testReflection() {
		Long res = StringNumberParser.textToNumber("twelve");
		System.out.println(res);
		res = StringNumberParser.textToNumber("ten");
		System.out.println(res);
		res = StringNumberParser.textToNumber("семнадцать");
		System.out.println(res);
	}

	@Test
	void parse1() {
		long number = 1;
		String text = "one";
		LangNumberParserEn parser = new LangNumberParserEn();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse10() {
		long number = 10;
		String text = "ten";
		LangNumberParserEn parser = new LangNumberParserEn();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse15() {
		long number = 15;
		String text = "fifteen";
		LangNumberParserEn parser = new LangNumberParserEn();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse40() {
		long number = 40;
		String text = "forty";
		LangNumberParserEn parser = new LangNumberParserEn();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse42() {
		long number = 42;
		String text = "forty-two";
		LangNumberParserEn parser = new LangNumberParserEn();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse100() {
		long number = 100;

		String text = "a Hundred";
		LangNumberParser parser = new LangNumberParserEn().with("onePrefix", "a");
		assertEquals(text.toLowerCase(), parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));

		text = "one Hundred";
		parser = new LangNumberParserEn().with("onePrefix", "one");

		assertEquals(text.toLowerCase(), parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse107() {
		long number = 107;
		String text = "a hundred and seven";
		LangNumberParser parser = new LangNumberParserEn().with("onePrefix", "a");
		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));

		text = "one hundred and seven";
		parser = new LangNumberParserEn().with("onePrefix", "one");
		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse118() {
		long number = 118;

		String text = "a hundred eighteen";
		LangNumberParser parser = new LangNumberParserEn().with("onePrefix", "a");
		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));

		text = "one hundred eighteen";
		parser = new LangNumberParserEn().with("onePrefix", "one");
		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse5_000() {
		long number = 5_000;
		String text = "five thousand";
		LangNumberParserEn parser = new LangNumberParserEn();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse1_000() {
		long number = 1_000;
		String text = "one thousand";
		LangNumberParserEn parser = new LangNumberParserEn();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse5_274() {
		long number = 5_274;
		String text = "five thousand two hundred seventy-four";
		LangNumberParserEn parser = new LangNumberParserEn();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}


	@Test
	void parse152_385_112_008() {
		long number = 152_385_112_008L;
		String text = "a hundred fifty-two billion three hundred eighty-five million a hundred twelve thousand and eight";
		LangNumberParser parser = new LangNumberParserEn().with("onePrefix", "a");
		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));

		text = "one hundred fifty-two billion three hundred eighty-five million one hundred twelve thousand and eight";
		parser = new LangNumberParserEn().with("onePrefix", "one");
		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parseStringNumberInvalidInput() {
		LangNumberParserEn parser = new LangNumberParserEn();
		assertThrows(RuntimeException.class, () -> parser.stringToNumber("five thousand two hundreds zaza seventy four"));
	}
}