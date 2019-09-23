package com.paybay.challenge.types;

import java.util.EmptyStackException;

public interface Stack<T> extends Iterable<T> {

    /**
     * Pushes an element onto a stack and returns the new stack.
     *
     * @param value The element to push onto the stack.
     * @return The new stack.
     */
    Stack<T> push(T value);

    /**
     * Pops the top element off the stack.
     *
     * @return The new stack; never {@code null}.
     * @throws EmptyStackException if the stack is empty.
     */
    Stack<T> pop();

    /**
     * Gets the element on the top of the stack.
     *
     * @return The element on the top of the stack.
     * @throws EmptyStackException if the stack is empty.
     */
    T peek();

    /**
     * Gets a value indicating whether this is the empty stack.
     *
     * @return {@code true} if this stack is empty; otherwise {@code false}.
     */
    boolean isEmpty();

    /**
     * Gets an empty stack.
     *
     * @return The empty stack.
     */
    Stack<T> clear();

}