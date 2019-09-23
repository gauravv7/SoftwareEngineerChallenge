package com.paybay.challenge.utils;

public enum TypeCheckUtil {
    ;

    /**
     * Throws an exception if an argument is {@code null}.
     *
     * @param value The argument value.
     * @param parameterName The parameter name.
     * @param <T> The type of the parameter.
     * @exception NullPointerException if {@code value} is {@code null}.
     */
    public static <T> void notNull(T value, String parameterName) {
        if (value == null) {
            failNullPointer(parameterName);
        }
    }

    /**
     * Throws a {@link NullPointerException}.
     *
     * @param parameterName The name of the parameter that was {@code null}.
     * @exception NullPointerException always.
     */
    private static void failNullPointer(String parameterName) {
        // Separating out this throwing operation helps with inlining of the caller
        throw new NullPointerException(parameterName);
    }
}
