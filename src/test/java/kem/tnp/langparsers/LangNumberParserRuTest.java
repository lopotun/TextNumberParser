package kem.tnp.langparsers;

import kem.tnp.common.langparsers.LangNumberParserRu;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Evgeny Kurtser on 08-Jan-22 at 11:43 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
class LangNumberParserRuTest {

	@Test
	void numberToString314_042_517_234_583() {
		long number = 314_042_517_234_583L;
		String text = "триста четырнадцать триллионов сорок два миллиарда пятьсот семнадцать миллионов двести тридцать четыре тысячи пятьсот восемьдесят три";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void numberToString42_517_234_583() {
		long number = 42_517_234_583L;
		String text = "сорок два миллиарда пятьсот семнадцать миллионов двести тридцать четыре тысячи пятьсот восемьдесят три";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void numberToString517() {
		long number = 517;
		String text = "пятьсот семнадцать";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void numberToString573() {
		long number = 573;
		String text = "пятьсот семьдесят три";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse1() {
		long number = 1;
		String text = "один";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse10() {
		long number = 10;
		String text = "десять";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse15() {
		long number = 15;
		String text = "пятнадцать";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse40() {
		long number = 40;
		String text = "сорок";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse42() {
		long number = 42;
		String text = "сорок два";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse100() {
		long number = 100;
		String text = "сто";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse107() {
		long number = 107;
		String text = "сто семь";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse118() {
		long number = 118;
		String text = "сто восемнадцать";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse5_000() {
		long number = 5_000;
		String text = "пять тысяч";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parse5_274() {
		long number = 5_274;
		String text = "пять тысяч двести семьдесят четыре";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}


	@Test
	void parse152_385_112_008() {
		long number = 152_385_112_008L;
		String text = "сто пятьдесят два миллиарда триста восемьдесят пять миллионов сто двенадцать тысяч восемь";
		LangNumberParserRu parser = new LangNumberParserRu();

		assertEquals(text, parser.numberToString(number));
		assertEquals(number, parser.stringToNumber(text));
	}

	@Test
	void parseStringNumberInvalidInput() {
		LangNumberParserRu parser = new LangNumberParserRu();
		assertThrows(RuntimeException.class, () -> parser.stringToNumber("пять тысяч двести zaza семьдесят четыре"));
	}
}