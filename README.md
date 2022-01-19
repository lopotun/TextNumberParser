# KEM-TNP

TNP -- Text Number Parser. TNP translates a word-written number like "_five thousands three hundreds and sixteen_" to
number -- **5316** and vise versa.

[![Jenkins Status](https://img.shields.io/jenkins/s/https/ci-builds.apache.org/job/Logging/job/log4j/job/release-2.x.svg)](https://ci-builds.apache.org/job/Logging/job/log4j/job/release-2.x/)
[![Travis Status](https://travis-ci.org/apache/logging-log4j2.svg?branch=master)](https://travis-ci.org/apache/logging-log4j2)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.logging.log4j/log4j-api.svg)](http://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api)

## Pull Requests on Github

By sending a pull request you grant me sufficient rights to use and release the submitted work under the Apache license.
You grant the same rights (copyright license, patent license, etc.) to the Apache Software Foundation as if you have
signed a Contributor License Agreement. For contributions that are judged to be non-trivial, you will be asked to
actually signing a Contributor License Agreement.

## Usage

Basic usage of the `TNP`:

```java
package com.example;

public class Example {
	public static void main(String... args) {
		Example x = new Example();
		x.parseEx1();
		x.parseEx2();
	}

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
}
```

You can find more examples in `test` directory.

### Your own parser should:

1. implement `common.LangNumberParser` interface;
2. be marked with `@LanguageHandler(languageCode = "<two_letters_language_code>")` annotation.

For example:

```java

@LanguageHandler(languageCode = "jp")
public class LangNumberParserJp implements LangNumberParser {
	//...
	public Long stringToNumber(String input) {
		//...
	}

	public String numberToString(Long input) {
		//...
	}
}
```

can be used to implement Japanese parser.

## Dictionary files

TNP tries to automatically detect the language of the input text. It uses a list of most common words in each supported
language. This list can be found in `resources/dict` directory. If you want Japanese to be auto-detected, you should
fill `jp.dict` file with most common Japanese words and put it in `resources/dict` directory.

## Requirements

* Java 8 or greater.

## Contribute

Pull requests are welcomed!!  
Here are some issues labeled
with [please contribute :heart:](https://github.com/lopotun/TextWithNumbers/issues?q=is%3Aissue+is%3Aopen+label%3A%22please+contribute+%E2%9D%A4%EF%B8%8F%22+label%3A%22good+first+issue%22)

Dual licenced with Apache 2 and [WTFPL](http://www.wtfpl.net/), just do what the fuck you want to.

*This library is published as an act of giving and generosity, from developers to developers, to promote knowledge
sharing and a--hole free working environments.  
Please feel free to use it, and to contribute to the developers' community in the same manner.*  
[PayPal](https://www.paypal.com/donate/?hosted_button_id=VRQBCYB8MNSYS)

_Cheers_