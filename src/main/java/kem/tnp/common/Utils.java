package kem.tnp.common;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Evgeny Kurtser on 08-Jan-22 at 10:47 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
public class Utils {

	public enum NumberPosition {ONES, TENS, HUNDREDS, GROUPS}

	/**
	 * FSM entry point.
	 */
	public static final StateWithNumber INIT = new StateWithNumber(ParsingState.S_Group, 0L);
	/**
	 * Indicates FSM unrecognized event.
	 */
	public static final StateWithNumber ERROR = new StateWithNumber(ParsingState.S_Error, -1L);

	public static class StateWithNumber {
		public ParsingState state;
		public Long number;

		public StateWithNumber(ParsingState state, Long number) {
			this.state = state;
			this.number = number;
		}

		@Override
		public String toString() {
			return state + " (" + number + ")";
		}
	}


	public static StateWithNumber getState(String s, Supplier<Map<NumberPosition, Map<String, StateWithNumber>>> f) {
		final Map<NumberPosition, Map<String, StateWithNumber>> map = f.get();
		StateWithNumber stateWithNumber = map.get(NumberPosition.ONES).get(s);
		if(stateWithNumber == null) {
			stateWithNumber = map.get(NumberPosition.TENS).get(s);
			if(stateWithNumber == null) {
				stateWithNumber = map.get(NumberPosition.HUNDREDS).get(s);
				if(stateWithNumber == null) {
					stateWithNumber = map.get(NumberPosition.GROUPS).get(s);
					if(stateWithNumber == null) {
						stateWithNumber = ERROR;
					}
				}
			}
		}
		return stateWithNumber;
	}

	/**
	 * Parses the given input using the supplied FSM<p/>
	 * <img src="../common/doc-files/StringToNumberRu.svg" alt="Foo">
	 *
	 * @param input       number in text form e.g. "fourteen thousands two hundreds forty-six"
	 * @param fsmSupplier language-specific FSM
	 * @return numeric form of the given input, e.g. 14246
	 * @throws UnrecognizedTokenException if the given input cannot be transformed to numeric value
	 */
	public static Long parseStringNumber(String input, Supplier<Map<Utils.NumberPosition, Map<String, Utils.StateWithNumber>>> fsmSupplier) throws UnrecognizedTokenException {
		long res = 0L, accum = 0L;
		Utils.StateWithNumber numberedState = Utils.INIT;

		final String[] strings = input.split("\\s+");   // Split the string: "sixty five" -> ["sixty", "five"]
		for(String s : strings) {
			// Get to next state based on the current string number
			numberedState = numberedState.state.nextState(s, fsmSupplier);
			if(numberedState == Utils.ERROR) {
				throw new UnrecognizedTokenException(String.format("Unrecognized token \"%s\"", s));
			}
			// This is the "millions", "thousands" delimiter.
			if(numberedState.state == ParsingState.S_Group) {
				accum *= numberedState.number; // Multiply accumulated value by 10^x
				res += accum;
				accum = 0L; // Reset accumulated value
			} else {
				accum += numberedState.number;
			}
		}
		res += accum;
		return res;
	}
}