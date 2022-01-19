package kem.tnp.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

/**
 * Created by Evgeny Kurtser on 09-Dec-21 at 8:02 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Tuple3<A, B, C> extends Tuple2<A, B> {
	@With
	protected C c;

	public Tuple3(A a, B b, C c) {
		super(a, b);
		this.c = c;
	}

	public static <A, B, C> Tuple3<A, B, C> of(A a, B b, C c) {
		return new Tuple3<>(a, b, c);
	}
}