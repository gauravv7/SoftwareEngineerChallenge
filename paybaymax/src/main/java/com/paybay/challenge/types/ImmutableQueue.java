package com.paybay.challenge.types;

import com.paybay.challenge.utils.TypeCheckUtil;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This Queue implementation takes in consideration of 2 stacks
 *  - backwards and forwards - both based upon singly linked list based stack impl
 * @param <T>
 */
public class ImmutableQueue<T> implements Queue<T> {

    /**
     * The singleton empty queue.
     */
    private static final ImmutableQueue<?> EMPTY_QUEUE = new ImmutableQueue<Object>(ImmutableStack.empty(), ImmutableStack.empty());

    /**
     * front side of the queue where elements are pushed
     */
    private final ImmutableStack<T> incoming;

    /**
     * rear side of the queue from which elements are polled.
     */
    private final ImmutableStack<T> outgoing;

    /**
     * The backing field for {@link #getIncomingReversed()}.
     */
    private ImmutableStack<T> incomingReversed;

    /**
     * Initializes a new instance of the {@link ImmutableQueue} class.
     *
     * @param outgoing The outgoing stack.
     * @param incoming The incoming stack.
     */
    private ImmutableQueue(ImmutableStack<T> outgoing, ImmutableStack<T> incoming) {
        TypeCheckUtil.notNull(outgoing, "outgoing");
        TypeCheckUtil.notNull(incoming, "incoming");

        this.outgoing = outgoing;
        this.incoming = incoming;
        this.incomingReversed = null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableQueue<T> enQueue(T value) {
        if (isEmpty()) {
            return new ImmutableQueue<T>(ImmutableStack.<T>empty().push(value), ImmutableStack.<T>empty());
        } else {
            return new ImmutableQueue<T>(outgoing, incoming.push(value));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableQueue<T> deQueue() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        ImmutableStack<T> f = outgoing.pop();
        if (!f.isEmpty()) {
            return new ImmutableQueue<T>(f, incoming);
        } else if (incoming.isEmpty()) {
            return empty();
        } else {
            return new ImmutableQueue<T>(getIncomingReversed(), ImmutableStack.<T>empty());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T head() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        return outgoing.peek();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return outgoing.isEmpty() && incoming.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableQueue<T> clear() {
        return empty();
    }

    /**
     * Creates a new immutable collection pre-filled with the specified item.
     *
     * @param <T> The type of items stored by the collection.
     * @param item The item to pre-populate.
     * @return The immutable collection.
     */
    public static <T> ImmutableQueue<T> create(T item) {
        return ImmutableQueue.<T>empty().enQueue(item);
    }

    /**
     * Creates a new immutable collection pre-filled with the specified items.
     *
     * @param <T> The type of items stored by the collection.
     * @param items The items to pre-populate.
     * @return The immutable collection.
     */
    public static <T> ImmutableQueue<T> create(T... items) {
        TypeCheckUtil.notNull(items, "items");

        ImmutableQueue<T> queue = empty();
        for (T item : items) {
            queue = queue.enQueue(item);
        }

        return queue;
    }

    /**
     * Creates a new immutable collection pre-filled with the specified items.
     *
     * @param <T> The type of items stored by the collection.
     * @param items The items to pre-populate.
     * @return The immutable collection.
     */
    public static <T> ImmutableQueue<T> createAll(Iterable<? extends T> items) {
        TypeCheckUtil.notNull(items, "items");

        ImmutableQueue<T> queue = empty();
        for (T item : items) {
            queue = queue.enQueue(item);
        }

        return queue;
    }

    public static <T> ImmutableQueue<T> empty() {
        @SuppressWarnings("unchecked")
        ImmutableQueue<T> result = (ImmutableQueue<T>)EMPTY_QUEUE;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return new Itr<T>(this);
    }

    private ImmutableStack<T> getIncomingReversed() {
        if (incomingReversed == null) {
            incomingReversed = incoming.reverse();
        }

        return incomingReversed;
    }

    private static final class Itr<T> implements Iterator<T> {

        /**
         * The original queue being enumerated.
         */
        private final ImmutableQueue<T> originalQueue;
        /**
         * The remaining forwards queue not yet enumerated.
         */
        private ImmutableStack<T> remainingForwardsStack;
        /**
         * The remaining backwards stack not yet enumerated. Its order is reversed when the field is first initialized.
         */
        private ImmutableStack<T> remainingBackwardsStack;

        /**
         * Initializes a new instance of the {@link Itr} class.
         *
         * @param originalQueue The queue to enumerate.
         */
        public Itr(ImmutableQueue<T> originalQueue) {
            this.originalQueue = originalQueue;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            if (remainingForwardsStack == null) {
                return !originalQueue.isEmpty();
            } else {
                return !remainingForwardsStack.isEmpty() || !remainingBackwardsStack.isEmpty();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T next() {
            if (remainingForwardsStack == null) {
                remainingForwardsStack = originalQueue.outgoing;
                remainingBackwardsStack = originalQueue.getIncomingReversed();
            }

            if (!remainingForwardsStack.isEmpty()) {
                T result = remainingForwardsStack.peek();
                remainingForwardsStack = remainingForwardsStack.pop();
                return result;
            } else if (!remainingBackwardsStack.isEmpty()) {
                T result = remainingBackwardsStack.peek();
                remainingBackwardsStack = remainingBackwardsStack.pop();
                return result;
            }

            throw new NoSuchElementException();
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
