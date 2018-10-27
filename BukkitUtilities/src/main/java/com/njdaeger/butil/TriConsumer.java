package com.njdaeger.butil;

import java.util.Objects;

/**
 * Represents an operation which accepts three input arguments and returns nothing.
 *
 * @param <F> The first argument of the operation
 * @param <S> The second argument of the operation
 * @param <T> The third argument of the operation
 */
public interface TriConsumer<F, S, T> {

    /**
     * Runs the operation with the given arguments
     *
     * @param first The first argument of the operation
     * @param second The second argument of the operation
     * @param third The third argument of the operation
     */
    void accept(F first, S second, T third);

    /**
     * Returns a consumer which performs this operation, then the operation given in the after parameter.
     *
     * @param after The operation to perform after thie first operation
     * @return an operation which performs after the first operation.
     */
    default TriConsumer<F, S, T> andThen(TriConsumer<? super F, ? super S, ? super T> after) {
        Objects.requireNonNull(after);

        return (f, s, t) -> {
            accept(f, s, t);
            after.accept(f, s, t);
        };
    }

}
