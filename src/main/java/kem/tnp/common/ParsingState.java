package kem.tnp.common;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Enum-based FSM.
 * Created by Evgeny Kurtser on 09-Jan-22 at 12:35 AM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a></p>
 * <img src="doc-files/StringToNumberRu.svg" alt="Foo">
 */
public enum ParsingState {
	/**
	 * This state indicates "group" delimiter such as "millions", "thousands" etc.
	 */
	S_Group {
		@Override
		boolean isStateAllowed(ParsingState ps) {
			return ps == S_100_900 || ps == S_20_90 || ps == S_10_19 || ps == S_1_9;
		}
	},
	S_100_900 {
		@Override
		boolean isStateAllowed(ParsingState ps) {
			return ps == S_20_90 || ps == S_10_19 || ps == S_1_9 || ps == S_Group;
		}
	},
	S_20_90 {
		@Override
		boolean isStateAllowed(ParsingState ps) {
			return ps == S_1_9 || ps == S_Group;
		}
	},
	S_10_19 {
		@Override
		boolean isStateAllowed(ParsingState ps) {
			return ps == S_Group;
		}
	},
	S_1_9 {
		@Override
		boolean isStateAllowed(ParsingState ps) {
			return ps == S_Group;
		}
	},
	S_Error {
		@Override
		boolean isStateAllowed(ParsingState ps) {
			return ps == S_Error;
		}
	};

	/**
	 * Checks whether the current state is allowed to transit to the given state
	 *
	 * @param ps new state to transit to
	 * @return <em>true</em> if the current state is allowed to transit to the given state
	 */
	abstract boolean isStateAllowed(ParsingState ps);

	/**
	 * Transit to next state based on the given event
	 *
	 * @param event text-number event (e.g. "sixty" or "a thousand")
	 * @return next state. If the given event is not supported on the current state then {@linkplain Utils#ERROR} is returned
	 */
	public Utils.StateWithNumber nextState(String event, Supplier<Map<Utils.NumberPosition, Map<String, Utils.StateWithNumber>>> f) {
		final Utils.StateWithNumber stateWithNumber = Utils.getState(event, f);
		return isStateAllowed(stateWithNumber.state) ? stateWithNumber : Utils.ERROR;
	}
}