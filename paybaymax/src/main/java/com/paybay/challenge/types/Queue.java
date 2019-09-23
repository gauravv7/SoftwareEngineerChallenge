package com.paybay.challenge.types;

import java.util.EmptyStackException;

public interface Queue<T> extends Iterable<T> {

    /**
     * Adds an element to the back of the queue.
     *
     * @param value The element to add to the queue.
     * @return The new queue.
     */
    public Queue<T> enQueue(T value);

    /**
     * Removes the element at the beginning of the immutable queue, and returns the new queue.
     *
     * @return The new queue; never {@code null}.
     * @throws EmptyStackException if the queue is empty.
     */
    public Queue<T> deQueue();

    /**
     * Gets the element at the front of the queue.
     *
     * @return The element at the front of the queue.
     * @throws EmptyStackException if the queue is empty.
     */
    public T head();

    /**
     * Gets a value indicating whether this is the empty queue.
     *
     * @return {@code true} if this queue is empty; otherwise {@code false}.
     */
    public boolean isEmpty();

    /**
     * helper - Gets an empty queue.
     *
     * @return The empty queue.
     */
    public Queue<T> clear();

}