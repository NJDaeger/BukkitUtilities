package com.njdaeger.butil;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a pair.
 * @param <F> The first type of the pair
 * @param <S> The second type of the pair
 */
public final class Pair<F, S> {
    
    private F first;
    private S second;
    
    /**
     * Create a new pair instance
     * @param first The first object to set
     * @param second The second object to set
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Sets the first object in this pair
     * @param first The object to set
     */
    public void setFirst(F first) {
        this.first = first;
    }
    
    /**
     * Sets the second object in this pair
     * @param second The object to set
     */
    public void setSecond(S second) {
        this.second = second;
    }
    
    /**
     * Get the first object in this pair
     * @return The first object
     */
    public F getFirst() {
        return first;
    }
    
    /**
     * Get the first object in this pair if it matches the given predicate
     * @param predicate The predicate needing to be matched
     * @param fallback The fallback value
     * @return The first object in this pair if it matches the predicate, otherwise the fallback value is returned.
     */
    public F getFirstOrElse(Predicate<F> predicate, F fallback) {
        if (isFirst(predicate)) return first;
        else return fallback;
    }
    
    /**
     * Transform the first object in this pair to another type.
     * @param type The type to transform the first object into
     * @param function The function to apply on the first object
     * @param <R> The type to transform into
     * @return The transformed first object
     */
    public <R> R transformFirst(Class<R> type, Function<F, R> function) {
        return function.apply(first);
    }
    
    /**
     * Get the second object in this pair
     * @return The second object
     */
    public S getSecond() {
        return second;
    }
    
    /**
     * Get the second object in this pair if it matches the given predicate
     * @param predicate The predicate needing to be matched
     * @param fallback The fallback value
     * @return The second object in this pair if it matches the predicate, otherwise the fallback value is returned.
     */
    public S getSecondOrElse(Predicate<S> predicate, S fallback) {
        if (isSecond(predicate)) return second;
        else return fallback;
    }
    
    /**
     * Transform the second object in this pair to another type.
     * @param type The type to transform the second object into
     * @param function The function to apply on the second object
     * @param <R> The type to transform into
     * @return The transformed second object
     */
    public <R> R transformSecond(Class<R> type, Function<S, R> function) {
        return function.apply(second);
    }
    
    /**
     * Tests the first object of this pair against a predicate
     * @param predicate The predicate to test against
     * @return True if the first object matches the predicate, false otherwise.
     */
    public boolean isFirst(Predicate<F> predicate) {
        return predicate.test(first);
    }
    
    /**
     * Tests the second object of this pair against a predicate
     * @param predicate The predicate to test against
     * @return True if the second object matches the predicate, false otherwise.
     */
    public boolean isSecond(Predicate<S> predicate) {
        return predicate.test(second);
    }
    
}
