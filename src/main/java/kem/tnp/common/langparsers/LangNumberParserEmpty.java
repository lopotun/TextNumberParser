package kem.tnp.common.langparsers;

import kem.tnp.common.LangNumberParser;
import kem.tnp.common.LanguageException;
import kem.tnp.common.LanguageHandler;

/**
 * This class contains methods that convert the given text input e.g. "fourteen thousands two hundreds forty-six" to its numeric form -- 14246 and vise versa<p/>
 * <p>
 * Created by Evgeny Kurtser on 09-Jan-22 at 7:28 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
@LanguageHandler(languageCode = "XX")
public class LangNumberParserEmpty implements LangNumberParser {
	private static final LangNumberParserEmpty INSTANCE = new LangNumberParserEmpty();

	public static LangNumberParserEmpty getInstance() {
		return INSTANCE;
	}

	public Long stringToNumber(String input) {
		throw new LanguageException("Empty parser doesn't support string parsing");
	}

	public String numberToString(Long input) {
		throw new LanguageException("Empty parser doesn't support number building");
	}
}