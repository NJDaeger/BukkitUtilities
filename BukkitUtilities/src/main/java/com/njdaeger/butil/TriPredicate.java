package com.njdaeger.butil;

/**
 * Represents a boolean value based function with three arguments
 *
 * @param <F> The first argument in the function
 * @param <S> The second argument in the function
 * @param <T> The third argument in the function
 */
public interface TriPredicate<F, S, T> {

    /**
     * Evaluates the predicate on the given arguments
     *
     * @param first The first argument
     * @param second The second argument
     * @param third The third argument
     * @return True if the arguments match the predicate, otherwise false.
     */
    boolean test(F first, S second, T third);



}
