package kem.tnp.common.langparsers;

import kem.tnp.common.LangNumberParser;
import kem.tnp.common.LanguageHandler;
import kem.tnp.common.ParsingState;
import kem.tnp.common.Utils;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This class contains methods that convert the given input e.g. "fourteen thousands two hundreds forty-six" to its numeric form -- 14246 and vise versa
 * Created by Evgeny Kurtser on 09-Jan-22 at 7:28 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
@LanguageHandler(languageCode = "en")
public class LangNumberParserEn implements LangNumberParser {
	private static final String REGEX_HUNDREDS = "(?i)\\s+(h)";      // five hundreds -> five_hundreds
	private static final String REGEX_XTY_Y = "(?i)ty\\s*-\\s*(\\w)";   // twenty-four -> twenty four
	private static final String REGEX_A_X = "(?i)a\\s+(thousand|million|billion|trillion)";  // a million -> one million

	private String onePrefix = "one";
	private boolean useHyphen = true;

	/**
	 * Configures this parser.
	 * This implementation recognizes the following configurations:<ul>
	 * <li>["onePrefix"->"a|one"] -- if "a" then 100 -> "<strong>a</strong> hundred"; if "one" then 100 -> "<strong>one</strong> hundred". Default is "<strong>one</strong>"</li>
	 * <li>["useHyphen"->"true|false"] -- if "true" then 24 -> "twenty<strong>-</strong>four"; if "false" then 24 -> "twenty<strong> </strong>four". Default is "<strong>-</strong>"</li>
	 * </ul>
	 * Unrecognized configuration won't affect this instance.
	 *
	 * @param key   configuration key
	 * @param value configuration value
	 * @return this instance with applied configuration
	 * @see LangNumberParser#with(Map)
	 */
	public LangNumberParser with(@NotNull String key, @NotNull Object value) {
		switch(key.toLowerCase()) {
			case "oneprefix":
				this.onePrefix = (String) value;
				break;
			case "usehyphen":
				this.useHyphen = Boolean.parseBoolean((String) value);
				break;
		}
		return this;
	}

	/**
	 * Converts the given input e.g. "fourteen thousands two hundreds forty-six" to its numeric form -- 14246<p/>
	 * This implementation uses {@link Utils#parseStringNumber(String, Supplier)} method providing English parser FSM.
	 *
	 * @param input number in text form e.g. "fourteen thousands two hundreds forty-six"
	 * @return numeric form of the given input
	 */
	public Long stringToNumber(String input) {
		String adapted = input
				.replaceAll(REGEX_HUNDREDS, "_$1")  // five hundreds -> five_hundreds
				.replaceAll(REGEX_XTY_Y, "ty $1")   // twenty-four -> twenty four
				.replaceAll(REGEX_A_X, "one $1")   // twenty-four -> twenty four
				.replace(" and", "")    // five hundreds and seven -> five hundreds seven
				.toLowerCase();
		Supplier<Map<Utils.NumberPosition, Map<String, Utils.StateWithNumber>>> mapSupplier = () -> NUM_POS;
		return Utils.parseStringNumber(adapted, mapSupplier);
	}

	/**
	 * Converts the given input e.g. 14246 to its text form -- "fourteen thousand two hundred and forty-six"
	 *
	 * @param input a number
	 * @return text form of the given input
	 */
	public String numberToString(Long input) {// 42_517_234_583L
		int n;
		String group;
		String res;
		long ten03 = (input / 1_000L); // 42_517_234
		n = (int) (input % 1_000L); // 583
		res = num2Str(n, "").toString();
		if(ten03 > 0) {
			long ten06 = (ten03 / 1_000L);
			n = (int) (ten03 % 1_000L); // 234
			group = " thousand ";
			res = num2Str(n, group) + res;
			if(ten06 > 0) {
				long ten09 = (ten06 / 1_000L);
				n = (int) (ten06 % 1_000L);
				group = " million ";
				res = num2Str(n, group) + res;
				if(ten09 > 0) {
					int ten12 = (int) (ten09 / 1_000L);
					n = (int) (ten09 % 1_000L); // 42
					group = " billion ";
					res = num2Str(n, group) + res;
					if(ten12 > 0) {
						n = (int) (ten12 % 1_000L); // 42
						group = " trillion ";
						res = num2Str(n, group) + res;
					}
				}
			}
		}
		if(res.equals("and one"))
			return "one";
		return res.trim();
	}

	private StringBuilder num2Str(int input, String group) {// 573 | 517
		StringBuilder res = new StringBuilder();
		int n;
		n = input / 100; // 5 | 5
		if(n > 0) {
			String hundred = HUNDREDS_MAP_R.get(n * 100);
			if(n == 1) {
				res.append(onePrefix).append(" ");
			}
			res.append(hundred); // 500-> "пятьсот" | 500-> "пятьсот"
		}

		input = input - (input / 100 * 100); // 73 | 17
		if(input == 0) {
			return res.append(group);
		}
		n = input / 10; // 7 | 1
		if(res.length() > 0) res.append(" ");
		if(n > 1) { // 20-90
			res.append(TENS_MAP_R.get(n * 10)); // 70-> "семьдесят"
			n = input % 10; // 3
			if(n > 0) {
				res.append(useHyphen ? "-" : " ").append(ONES_MAP_R.get(n)); // 3-> "три"
			}
		} else { // 11-19 or 0
			if(n == 1) { // 11-19
				res.append(TENS_MAP_R.get(input)); // 17-> "семнадцать"
			} else { // 0
				if(group.isEmpty())
					res.append("and ");
				res.append(ONES_MAP_R.get(input)); // 3-> "три"
			}
		}
		return res.append(group);
	}

	private static final Map<String, Utils.StateWithNumber> ONES_MAP = new HashMap<>();
	private static final Map<Integer, String> ONES_MAP_R = new HashMap<>();

	private static final Map<String, Utils.StateWithNumber> TENS_MAP = new HashMap<>();
	private static final Map<Integer, String> TENS_MAP_R = new HashMap<>();

	private static final Map<String, Utils.StateWithNumber> HUNDREDS_MAP = new HashMap<>();
	private static final Map<Integer, String> HUNDREDS_MAP_R = new HashMap<>();

	private static final Map<String, Utils.StateWithNumber> GROUPS_MAP = new HashMap<>();
	private static final Map<Utils.NumberPosition, Map<String, Utils.StateWithNumber>> NUM_POS = new HashMap<>();

	static {
		// ---------------- ONES ----------------
		ONES_MAP.put("one", new Utils.StateWithNumber(ParsingState.S_1_9, 1L));
		ONES_MAP.put("two", new Utils.StateWithNumber(ParsingState.S_1_9, 2L));
		ONES_MAP.put("three", new Utils.StateWithNumber(ParsingState.S_1_9, 3L));
		ONES_MAP.put("four", new Utils.StateWithNumber(ParsingState.S_1_9, 4L));
		ONES_MAP.put("five", new Utils.StateWithNumber(ParsingState.S_1_9, 5L));
		ONES_MAP.put("six", new Utils.StateWithNumber(ParsingState.S_1_9, 6L));
		ONES_MAP.put("seven", new Utils.StateWithNumber(ParsingState.S_1_9, 7L));
		ONES_MAP.put("eight", new Utils.StateWithNumber(ParsingState.S_1_9, 8L));
		ONES_MAP.put("nine", new Utils.StateWithNumber(ParsingState.S_1_9, 9L));

		ONES_MAP_R.put(1, "one");
		ONES_MAP_R.put(2, "two");
		ONES_MAP_R.put(3, "three");
		ONES_MAP_R.put(4, "four");
		ONES_MAP_R.put(5, "five");
		ONES_MAP_R.put(6, "six");
		ONES_MAP_R.put(7, "seven");
		ONES_MAP_R.put(8, "eight");
		ONES_MAP_R.put(9, "nine");


		// ---------------- TENS ----------------
		TENS_MAP.put("ten", new Utils.StateWithNumber(ParsingState.S_10_19, 10L));
		TENS_MAP.put("eleven", new Utils.StateWithNumber(ParsingState.S_10_19, 11L));
		TENS_MAP.put("twelve", new Utils.StateWithNumber(ParsingState.S_10_19, 12L));
		TENS_MAP.put("thirteen", new Utils.StateWithNumber(ParsingState.S_10_19, 13L));
		TENS_MAP.put("fourteen", new Utils.StateWithNumber(ParsingState.S_10_19, 14L));
		TENS_MAP.put("fifteen", new Utils.StateWithNumber(ParsingState.S_10_19, 15L));
		TENS_MAP.put("sixteen", new Utils.StateWithNumber(ParsingState.S_10_19, 16L));
		TENS_MAP.put("seventeen", new Utils.StateWithNumber(ParsingState.S_10_19, 17L));
		TENS_MAP.put("eighteen", new Utils.StateWithNumber(ParsingState.S_10_19, 18L));
		TENS_MAP.put("nineteen", new Utils.StateWithNumber(ParsingState.S_10_19, 19L));
		TENS_MAP.put("twenty", new Utils.StateWithNumber(ParsingState.S_20_90, 20L));
		TENS_MAP.put("thirty", new Utils.StateWithNumber(ParsingState.S_20_90, 30L));
		TENS_MAP.put("forty", new Utils.StateWithNumber(ParsingState.S_20_90, 40L));
		TENS_MAP.put("fifty", new Utils.StateWithNumber(ParsingState.S_20_90, 50L));
		TENS_MAP.put("sixty", new Utils.StateWithNumber(ParsingState.S_20_90, 60L));
		TENS_MAP.put("seventy", new Utils.StateWithNumber(ParsingState.S_20_90, 70L));
		TENS_MAP.put("eighty", new Utils.StateWithNumber(ParsingState.S_20_90, 80L));
		TENS_MAP.put("ninety", new Utils.StateWithNumber(ParsingState.S_20_90, 90L));

		TENS_MAP_R.put(10, "ten");
		TENS_MAP_R.put(11, "eleven");
		TENS_MAP_R.put(12, "twelve");
		TENS_MAP_R.put(13, "thirteen");
		TENS_MAP_R.put(14, "fourteen");
		TENS_MAP_R.put(15, "fifteen");
		TENS_MAP_R.put(16, "sixteen");
		TENS_MAP_R.put(17, "seventeen");
		TENS_MAP_R.put(18, "eighteen");
		TENS_MAP_R.put(19, "nineteen");
		TENS_MAP_R.put(20, "twenty");
		TENS_MAP_R.put(30, "thirty");
		TENS_MAP_R.put(40, "forty");
		TENS_MAP_R.put(50, "fifty");
		TENS_MAP_R.put(60, "sixty");
		TENS_MAP_R.put(70, "seventy");
		TENS_MAP_R.put(80, "eighty");
		TENS_MAP_R.put(90, "ninety");


		// ---------------- HUNDREDS ----------------
		HUNDREDS_MAP.put("hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 100L));
		HUNDREDS_MAP.put("a_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 100L));
		HUNDREDS_MAP.put("one_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 100L));
		HUNDREDS_MAP.put("two_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 200L));
		HUNDREDS_MAP.put("three_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 300L));
		HUNDREDS_MAP.put("four_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 400L));
		HUNDREDS_MAP.put("five_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 500L));
		HUNDREDS_MAP.put("six_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 600L));
		HUNDREDS_MAP.put("seven_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 700L));
		HUNDREDS_MAP.put("eight_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 800L));
		HUNDREDS_MAP.put("nine_hundred", new Utils.StateWithNumber(ParsingState.S_100_900, 900L));

		HUNDREDS_MAP_R.put(100, "hundred");
		HUNDREDS_MAP_R.put(200, "two hundred");
		HUNDREDS_MAP_R.put(300, "three hundred");
		HUNDREDS_MAP_R.put(400, "four hundred");
		HUNDREDS_MAP_R.put(500, "five hundred");
		HUNDREDS_MAP_R.put(600, "six hundred");
		HUNDREDS_MAP_R.put(700, "seven hundred");
		HUNDREDS_MAP_R.put(800, "eight hundred");
		HUNDREDS_MAP_R.put(900, "nine hundred");

		// ---------------- GROUPS ----------------
		GROUPS_MAP.put("thousand", new Utils.StateWithNumber(ParsingState.S_Group, 1_000L));
		GROUPS_MAP.put("a_thousand", new Utils.StateWithNumber(ParsingState.S_Group, 1_000L));
		GROUPS_MAP.put("million", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000L));
		GROUPS_MAP.put("billion", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000_000L));
		GROUPS_MAP.put("trillion", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000_000_000L));

		NUM_POS.put(Utils.NumberPosition.ONES, ONES_MAP);
		NUM_POS.put(Utils.NumberPosition.TENS, TENS_MAP);
		NUM_POS.put(Utils.NumberPosition.HUNDREDS, HUNDREDS_MAP);
		NUM_POS.put(Utils.NumberPosition.GROUPS, GROUPS_MAP);
	}
}