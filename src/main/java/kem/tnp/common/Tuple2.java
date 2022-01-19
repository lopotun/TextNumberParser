package kem.tnp.common;

import lombok.*;

/**
 * Created by Evgeny Kurtser on 09-Dec-21 at 8:02 PM.
 * <a href=mailto:lopotun@gmail.com>lopotun@gmail.com</a>
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tuple2<A, B> {
	@With
	protected A a;
	@With
	protected B b;

	public static <A, B> Tuple2<A, B> of(A a, B b) {
		return new Tuple2<>(a, b);
	}
}