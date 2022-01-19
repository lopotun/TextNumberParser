package kem.tnp.common.langparsers;

import kem.tnp.common.LangNumberParser;
import kem.tnp.common.LanguageHandler;
import kem.tnp.common.ParsingState;
import kem.tnp.common.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This class contains methods that convert the given input e.g. "четырнадцать тысяч двести сорок шесть" to its numeric form -- 14246 and vise versa<p/>
 * <p>
 * Created by Evgeny Kurtser on 09-Jan-22 at 7:28 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
@LanguageHandler(languageCode = "ru")
public class LangNumberParserRu implements LangNumberParser {

	/**
	 * Converts the given input e.g. "четырнадцать тысяч двести сорок шесть" to its numeric form -- 14246<p/>
	 * This implementation uses {@link Utils#parseStringNumber(String, Supplier)} method providing Russian parser FSM.
	 *
	 * @param input number as text e.g. "четырнадцать тысяч двести сорок шесть"
	 * @return numeric form of the given input
	 * This method must be marked with {@link LanguageHandler} annotation. The {@link LanguageHandler#languageCode()} parameter indicates language (code) that is covered by this parser
	 */
	public Long stringToNumber(String input) {
		Supplier<Map<Utils.NumberPosition, Map<String, Utils.StateWithNumber>>> mapSupplier = () -> NUM_POS;
		return Utils.parseStringNumber(input, mapSupplier);
	}

	/**
	 * Converts the given input e.g. 14246 to its text form -- "четырнадцать тысяч двести сорок шесть"
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
			switch(n % 10) {
				case 1:
					group = " тысяча ";
					break;
				case 2:
				case 3:
				case 4:
					if(n % 100 > 11 && n % 100 < 15) group = " тысяч ";
					else group = " тысячи ";
					break;
				default:
					group = " тысяч ";
			}
			res = num2Str(n, group) + res;
			if(ten06 > 0) {
				long ten09 = (ten06 / 1_000L);
				n = (int) (ten06 % 1_000L);
				switch(n % 10) {
					case 1:
						group = " миллион ";
						break;
					case 2:
					case 3:
					case 4:
						if(n % 100 > 11 && n % 100 < 15) group = " миллионов ";
						else group = " миллиона ";
						break;
					default:
						group = " миллионов ";
				}
				res = num2Str(n, group) + res;
				if(ten09 > 0) {
					int ten12 = (int) (ten09 / 1_000L);
					n = (int) (ten09 % 1_000L); // 42
					switch(n % 10) {
						case 1:
							group = " миллиард ";
							break;
						case 2:
						case 3:
						case 4:
							if(n % 100 > 11 && n % 100 < 15) group = " миллиардов ";
							else group = " миллиарда ";
							break;
						default:
							group = " миллиардов ";
					}
					res = num2Str(n, group) + res;
					if(ten12 > 0) {
						n = (int) (ten12 % 1_000L); // 42
						switch(n % 10) {
							case 1:
								group = " триллион ";
								break;
							case 2:
							case 3:
							case 4:
								if(n % 100 > 11 && n % 100 < 15) group = " триллионов ";
								else group = " триллиона ";
								break;
							default:
								group = " триллионов ";
						}
						res = num2Str(n, group) + res;
					}
				}
			}
		}
		return res.trim();
	}

	private StringBuilder num2Str(int input, String group) {// 573 | 517
		StringBuilder res = new StringBuilder();
		int n;
		n = input / 100; // 5 | 5
		if(n > 0) {
			res.append(HUNDREDS_MAP_R.get(n * 100)); // 500-> "пятьсот" | 500-> "пятьсот"
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
			if(group.equalsIgnoreCase(" тысяча ") || group.equalsIgnoreCase(" тысячи ")) {
				res.append(" ");
				switch(n) {
					case 1:
					case 2:
						res.append(ONES_MAP_R.get(n * 10));
					default:
						res.append(ONES_MAP_R.get(n));
				}
			} else {
				if(n > 0) {
					res.append(" ").append(ONES_MAP_R.get(n)); // 3-> "три"
				}
			}
		} else { // 11-19 or 0
			if(n == 1) { // 11-19
				res.append(TENS_MAP_R.get(input)); // 17-> "семнадцать"
			} else { // 0
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
		ONES_MAP.put("один", new Utils.StateWithNumber(ParsingState.S_1_9, 1L));
		ONES_MAP.put("одна", new Utils.StateWithNumber(ParsingState.S_1_9, 1L));
		ONES_MAP.put("два", new Utils.StateWithNumber(ParsingState.S_1_9, 2L));
		ONES_MAP.put("две", new Utils.StateWithNumber(ParsingState.S_1_9, 2L));
		ONES_MAP.put("три", new Utils.StateWithNumber(ParsingState.S_1_9, 3L));
		ONES_MAP.put("четыре", new Utils.StateWithNumber(ParsingState.S_1_9, 4L));
		ONES_MAP.put("пять", new Utils.StateWithNumber(ParsingState.S_1_9, 5L));
		ONES_MAP.put("шесть", new Utils.StateWithNumber(ParsingState.S_1_9, 6L));
		ONES_MAP.put("семь", new Utils.StateWithNumber(ParsingState.S_1_9, 7L));
		ONES_MAP.put("восемь", new Utils.StateWithNumber(ParsingState.S_1_9, 8L));
		ONES_MAP.put("девять", new Utils.StateWithNumber(ParsingState.S_1_9, 9L));

		ONES_MAP_R.put(1, "один");
		ONES_MAP_R.put(10, "одна");
		ONES_MAP_R.put(2, "два");
		ONES_MAP_R.put(20, "две");
		ONES_MAP_R.put(3, "три");
		ONES_MAP_R.put(4, "четыре");
		ONES_MAP_R.put(5, "пять");
		ONES_MAP_R.put(6, "шесть");
		ONES_MAP_R.put(7, "семь");
		ONES_MAP_R.put(8, "восемь");
		ONES_MAP_R.put(9, "девять");


		// ---------------- TENS ----------------
		TENS_MAP.put("десять", new Utils.StateWithNumber(ParsingState.S_10_19, 10L));
		TENS_MAP.put("одинадцать", new Utils.StateWithNumber(ParsingState.S_10_19, 11L));
		TENS_MAP.put("двенадцать", new Utils.StateWithNumber(ParsingState.S_10_19, 12L));
		TENS_MAP.put("тринадцать", new Utils.StateWithNumber(ParsingState.S_10_19, 13L));
		TENS_MAP.put("четырнадцать", new Utils.StateWithNumber(ParsingState.S_10_19, 14L));
		TENS_MAP.put("пятнадцать", new Utils.StateWithNumber(ParsingState.S_10_19, 15L));
		TENS_MAP.put("шестнадцать", new Utils.StateWithNumber(ParsingState.S_10_19, 16L));
		TENS_MAP.put("семнадцать", new Utils.StateWithNumber(ParsingState.S_10_19, 17L));
		TENS_MAP.put("восемнадцать", new Utils.StateWithNumber(ParsingState.S_10_19, 18L));
		TENS_MAP.put("девятнадцать", new Utils.StateWithNumber(ParsingState.S_10_19, 19L));
		TENS_MAP.put("двадцать", new Utils.StateWithNumber(ParsingState.S_20_90, 20L));
		TENS_MAP.put("тридцать", new Utils.StateWithNumber(ParsingState.S_20_90, 30L));
		TENS_MAP.put("сорок", new Utils.StateWithNumber(ParsingState.S_20_90, 40L));
		TENS_MAP.put("пятьдесят", new Utils.StateWithNumber(ParsingState.S_20_90, 50L));
		TENS_MAP.put("шестьдесят", new Utils.StateWithNumber(ParsingState.S_20_90, 60L));
		TENS_MAP.put("семьдесят", new Utils.StateWithNumber(ParsingState.S_20_90, 70L));
		TENS_MAP.put("восемьдесят", new Utils.StateWithNumber(ParsingState.S_20_90, 80L));
		TENS_MAP.put("девяносто", new Utils.StateWithNumber(ParsingState.S_20_90, 90L));

		TENS_MAP_R.put(10, "десять");
		TENS_MAP_R.put(11, "одинадцать");
		TENS_MAP_R.put(12, "двенадцать");
		TENS_MAP_R.put(13, "тринадцать");
		TENS_MAP_R.put(14, "четырнадцать");
		TENS_MAP_R.put(15, "пятнадцать");
		TENS_MAP_R.put(16, "шестнадцать");
		TENS_MAP_R.put(17, "семнадцать");
		TENS_MAP_R.put(18, "восемнадцать");
		TENS_MAP_R.put(19, "девятнадцать");
		TENS_MAP_R.put(20, "двадцать");
		TENS_MAP_R.put(30, "тридцать");
		TENS_MAP_R.put(40, "сорок");
		TENS_MAP_R.put(50, "пятьдесят");
		TENS_MAP_R.put(60, "шестьдесят");
		TENS_MAP_R.put(70, "семьдесят");
		TENS_MAP_R.put(80, "восемьдесят");
		TENS_MAP_R.put(90, "девяносто");


		// ---------------- HUNDREDS ----------------
		HUNDREDS_MAP.put("сто", new Utils.StateWithNumber(ParsingState.S_100_900, 100L));
		HUNDREDS_MAP.put("двести", new Utils.StateWithNumber(ParsingState.S_100_900, 200L));
		HUNDREDS_MAP.put("триста", new Utils.StateWithNumber(ParsingState.S_100_900, 300L));
		HUNDREDS_MAP.put("четыреста", new Utils.StateWithNumber(ParsingState.S_100_900, 400L));
		HUNDREDS_MAP.put("пятьсот", new Utils.StateWithNumber(ParsingState.S_100_900, 500L));
		HUNDREDS_MAP.put("шестьсот", new Utils.StateWithNumber(ParsingState.S_100_900, 600L));
		HUNDREDS_MAP.put("семьсот", new Utils.StateWithNumber(ParsingState.S_100_900, 700L));
		HUNDREDS_MAP.put("восемьсот", new Utils.StateWithNumber(ParsingState.S_100_900, 800L));
		HUNDREDS_MAP.put("девятсот", new Utils.StateWithNumber(ParsingState.S_100_900, 900L));

		HUNDREDS_MAP_R.put(100, "сто");
		HUNDREDS_MAP_R.put(200, "двести");
		HUNDREDS_MAP_R.put(300, "триста");
		HUNDREDS_MAP_R.put(400, "четыреста");
		HUNDREDS_MAP_R.put(500, "пятьсот");
		HUNDREDS_MAP_R.put(600, "шестьсот");
		HUNDREDS_MAP_R.put(700, "семьсот");
		HUNDREDS_MAP_R.put(800, "восемьсот");
		HUNDREDS_MAP_R.put(900, "девятсот");


		// ---------------- GROUPS ----------------
		GROUPS_MAP.put("тысяч", new Utils.StateWithNumber(ParsingState.S_Group, 1_000L));
		GROUPS_MAP.put("тысяча", new Utils.StateWithNumber(ParsingState.S_Group, 1_000L));
		GROUPS_MAP.put("тысячи", new Utils.StateWithNumber(ParsingState.S_Group, 1_000L));
		GROUPS_MAP.put("миллион", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000L));
		GROUPS_MAP.put("миллиона", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000L));
		GROUPS_MAP.put("миллионов", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000L));
		GROUPS_MAP.put("миллиард", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000_000L));
		GROUPS_MAP.put("миллиарда", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000_000L));
		GROUPS_MAP.put("миллиардов", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000_000L));
		GROUPS_MAP.put("триллион", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000_000_000L));
		GROUPS_MAP.put("триллиона", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000_000_000L));
		GROUPS_MAP.put("триллионов", new Utils.StateWithNumber(ParsingState.S_Group, 1_000_000_000_000L));


		NUM_POS.put(Utils.NumberPosition.ONES, ONES_MAP);
		NUM_POS.put(Utils.NumberPosition.TENS, TENS_MAP);
		NUM_POS.put(Utils.NumberPosition.HUNDREDS, HUNDREDS_MAP);
		NUM_POS.put(Utils.NumberPosition.GROUPS, GROUPS_MAP);
	}
}