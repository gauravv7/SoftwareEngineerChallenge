package com.paybay.challenge.types;

import com.paybay.challenge.utils.TypeCheckUtil;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a Singly linked list based Stack
 * @param <T>
 */
public class ImmutableStack<T> implements Stack<T> {

    private static final ImmutableStack<?> EMPTY_STACK = new ImmutableStack<Object>();

    /**
     * The element on the top of the stack.
     */
    private final T head;

    /**
     * A stack that contains the rest of the elements (under the top element);
     */
    private final ImmutableStack<T> tail;

    /**
     * Initializes a new instance of the {@link ImmutableStack} class that acts as the empty stack.
     */
    private ImmutableStack() {
        head = null;
        tail = null;
    }

    /**
     * Initializes a new instance of the {@link ImmutableStack} class.
     *
     * @param head The head element on the stack.
     * @param tail The rest of the elements on the stack.
     */
    private ImmutableStack(T head, ImmutableStack<T> tail) {
        TypeCheckUtil.notNull(tail, "tail");

        this.head = head;
        this.tail = tail;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableStack<T> push(T value) {
        return new ImmutableStack<T>(value, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableStack<T> pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        return tail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        return head;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return tail == null;
    }

    /**
     * Gets the empty stack, upon which all stacks are built.
     *
     * @param <T> The type of element stored by the stack.
     * @return The empty stack.
     */
    public static <T> ImmutableStack<T> empty() {
        @SuppressWarnings("unchecked")
        ImmutableStack<T> result = (ImmutableStack<T>)EMPTY_STACK;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableStack<T> clear() {
        return empty();
    }

    /**
     * Creates a new immutable collection pre-filled with the specified item.
     *
     * @param <T> The type of items stored by the collection.
     * @param item The item to pre-populate.
     * @return The immutable collection.
     */
    public static <T> ImmutableStack<T> create(T item) {
        return ImmutableStack.<T>empty().push(item);
    }

    /**
     * Creates a new immutable collection pre-filled with the specified items.
     *
     * @param <T> The type of items stored by the collection.
     * @param items The items to pre-populate.
     * @return The immutable collection.
     */
    public static <T> ImmutableStack<T> create(T... items) {
        TypeCheckUtil.notNull(items, "items");

        ImmutableStack<T> stack = empty();
        for (T item : items) {
            stack = stack.push(item);
        }

        return stack;
    }

    /**
     * Creates a new immutable collection pre-filled with the specified items.
     *
     * @param <T> The type of items stored by the collection.
     * @param items The items to pre-populate.
     * @return The immutable collection.
     */
    public static <T> ImmutableStack<T> createAll(Iterable<? extends T> items) {
        TypeCheckUtil.notNull(items, "items");

        ImmutableStack<T> stack = empty();
        for (T item : items) {
            stack = stack.push(item);
        }

        return stack;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return new Itr<T>(this);
    }

    /**
     * Reverses the order of the stack.
     *
     * @return The reversed stack.
     */
    ImmutableStack<T> reverse() {
        ImmutableStack<T> result = clear();
        for (ImmutableStack<T> f = this; !f.isEmpty(); f = f.pop()) {
            result = result.push(f.peek());
        }

        return result;
    }

    private static final class Itr<T> implements Iterator<T> {

        /**
         * The original stack being enumerated.
         */
        private final ImmutableStack<T> originalStack;
        /**
         * The remaining stack not yet enumerated.
         */
        private ImmutableStack<T> remainingStack;

        /**
         * Initializes a new instance of the {@link Itr} class.
         *
         * @param originalStack The stack to enumerate.
         */
        public Itr(ImmutableStack<T> originalStack) {
            this.originalStack = originalStack;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            if (remainingStack == null) {
                return !originalStack.isEmpty();
            }

            return !remainingStack.isEmpty();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T next() {
            if (remainingStack == null) {
                remainingStack = originalStack;
            }

            if (remainingStack.isEmpty()) {
                throw new NoSuchElementException();
            }

            T result = remainingStack.peek();
            remainingStack = remainingStack.pop();
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
