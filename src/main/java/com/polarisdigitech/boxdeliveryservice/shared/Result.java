package com.polarisdigitech.boxdeliveryservice.shared;

import java.util.function.Function;

/**
 * Represents the outcome of a domain operation that can fail.
 * @param <T> the success value type
 * @param <E> the error type
 */

public final class Result<T, E> {
    private final T value;
    private final E error;
    private final boolean success;

    private  Result(T value, E error, boolean success) {
        this.value = value;
        this.error = error;
        this.success = success;
    }

    public static <T,E> Result<T, E> success(T value) {
        return new Result<>(value, null, true);
    }

    public static <T, E> Result<T, E> failure(E error) {
        if (error == null) {
            throw new IllegalArgumentException("error must not be null for a failure");
        }
        return new Result<>(null, error, false);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public T getValue() {
        if (!success) {
            throw new IllegalStateException("Cannot get value of a failed Result. Error: " + error);
        }
        return value;
    }

    public E getError() {
        if (success) {
            throw new IllegalStateException("Cannot get error of a successful Result.");
        }
        return error;
    }

    public <U> Result<U, E> map(Function<T, U> mapper) {
        if (isFailure()) {
            return Result.failure(error);
        }
        return Result.success(mapper.apply(value));
    }

    public <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper) {
        if (isFailure()) {
            return Result.failure(error);
        }
        return mapper.apply(value);
    }

    public T orElse(T fallback) {
        return success ? value : fallback;
    }

    @Override
    public String toString() {
        return success ? "Result.success(" + value + ")" : "Result.failure(" + error + ")";
    }
}
