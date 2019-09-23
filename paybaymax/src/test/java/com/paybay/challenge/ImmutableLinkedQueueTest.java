package com.paybay.challenge;

import com.google.common.collect.Iterables;
import com.paybay.challenge.types.ImmutableQueue;
import com.paybay.challenge.types.Queue;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.hamcrest.CoreMatchers.instanceOf;

public class ImmutableLinkedQueueTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void create() {
        ImmutableQueue<Integer> queue = ImmutableQueue.create();
        Assert.assertTrue(queue.isEmpty());

        queue = ImmutableQueue.create(1);
        Assert.assertFalse(queue.isEmpty());
        assertEqualSequences(Collections.singletonList(1), queue);

        queue = ImmutableQueue.create(1, 2);
        Assert.assertFalse(queue.isEmpty());
        assertEqualSequences(Arrays.asList(1, 2), queue);

        queue = ImmutableQueue.createAll(Arrays.asList(1, 2));
        Assert.assertFalse(queue.isEmpty());
        assertEqualSequences(Arrays.asList(1, 2), queue);
    }


    @Test
    public void enQueueDeQueueTest() {
        this.enQueueDeQueueTestHelper(new IntDataTestEqualityHelper(1), new IntDataTestEqualityHelper(2), new IntDataTestEqualityHelper(3));
        this.<IntDataTestEqualityHelper>enQueueDeQueueTestHelper();

        // interface test
        Queue<IntDataTestEqualityHelper> queueInterface = ImmutableQueue.create();
        Queue<IntDataTestEqualityHelper> populatedQueueInterface = queueInterface.enQueue(new IntDataTestEqualityHelper(5));
        Assert.assertEquals(new IntDataTestEqualityHelper(5), populatedQueueInterface.head());
    }

    @Test
    public void deQueuedValue() {
        ImmutableQueue<Integer> queue = ImmutableQueue.<Integer>empty().enQueue(5).enQueue(6);
        int head = queue.head();
        queue = queue.deQueue();
        Assert.assertEquals(5, head);
        head = queue.head();
        ImmutableQueue<Integer> emptyQueue = queue.deQueue();
        Assert.assertEquals(6, head);
        Assert.assertTrue(emptyQueue.isEmpty());

        // Also check that the interface extension method works.
        Queue<Integer> interfaceQueue = queue;
        head = interfaceQueue.head();
        Assert.assertSame(emptyQueue, interfaceQueue.deQueue());
        Assert.assertEquals(6, head);
    }

    @Test
    public void clearTheQueueTest() {
        ImmutableQueue<IntDataTestEqualityHelper> emptyQueue = ImmutableQueue.create();
        Assert.assertSame(emptyQueue, emptyQueue.clear());
        ImmutableQueue<IntDataTestEqualityHelper> nonEmptyQueue = emptyQueue.enQueue(new IntDataTestEqualityHelper(3));
        Assert.assertSame(emptyQueue, nonEmptyQueue.clear());

        // Interface test
        Assert.assertSame(emptyQueue, ((Queue<IntDataTestEqualityHelper>) nonEmptyQueue).clear());
    }

    @Test
    @SuppressWarnings({ "ObjectEqualsNull", "IncompatibleEquals" })
    public void equalsTest() {
        Assert.assertFalse(ImmutableQueue.<Integer>empty().equals(null));
        Assert.assertFalse(ImmutableQueue.<Integer>empty().equals("any positive value will asset false"));
        Assert.assertTrue(ImmutableQueue.<Integer>empty().equals(ImmutableQueue.<Integer>empty()));
        Assert.assertFalse(ImmutableQueue.<Integer>empty().enQueue(3).equals(ImmutableQueue.<Integer>empty().enQueue(3)));
        Assert.assertFalse(ImmutableQueue.<Integer>empty().enQueue(5).equals(ImmutableQueue.<Integer>empty().enQueue(3)));
        Assert.assertFalse(ImmutableQueue.<Integer>empty().enQueue(3).enQueue(5).equals(ImmutableQueue.<Integer>empty().enQueue(3)));
        Assert.assertFalse(ImmutableQueue.<Integer>empty().enQueue(3).equals(ImmutableQueue.<Integer>empty().enQueue(3).enQueue(5)));

        // Also be sure to compare equality of partially polled queues since that moves data to different fields.
        Assert.assertFalse(ImmutableQueue.<Integer>empty().enQueue(3).enQueue(1).enQueue(2).deQueue().equals(ImmutableQueue.<Integer>empty().enQueue(1).enQueue(2)));
    }

    @Test
    public void peekEmptyThrows() {
        thrown.expect(instanceOf(EmptyStackException.class));
        ImmutableQueue.<IntDataTestEqualityHelper>empty().head();
    }

    @Test
    public void deQueueEmptyThrows() {
        thrown.expect(instanceOf(EmptyStackException.class));
        ImmutableQueue.<IntDataTestEqualityHelper>empty().deQueue();
    }

    @Test
    public void empty() {
        // We already test create(), so just prove that empty has the same effect.
        Assert.assertSame(ImmutableQueue.<Integer>create(), ImmutableQueue.<Integer>empty());
    }

    @Test
    public void iterationOrder() {
        ImmutableQueue<Integer> queue = ImmutableQueue.empty();

        // Push elements onto the backwards stack.
        queue = queue.enQueue(1).enQueue(2).enQueue(3);
        Assert.assertEquals(1, (int)queue.head());

        // Force the backwards stack to be reversed and put into forwards.
        queue = queue.deQueue();

        // Push elements onto the backwards stack again.
        queue = queue.enQueue(4).enQueue(5);

        // Now that we have some elements on the forwards and backwards stack,
        // 1. enumerate all elements to verify order.
        assertEqualSequences(Arrays.asList(2, 3, 4, 5), queue);

        // 2. deQueue all elements to verify order
        int[] actual = new int[Iterables.size(queue)];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = queue.head();
            queue = queue.deQueue();
        }
    }

    @Test
    public void getEnumeratorText() {
        ImmutableQueue<Integer> queue = ImmutableQueue.create(5);
        Iterator<Integer> enumeratorStruct = queue.iterator();
        Assert.assertTrue(enumeratorStruct.hasNext());
        Assert.assertEquals(5, (int)enumeratorStruct.next());
        Assert.assertFalse(enumeratorStruct.hasNext());
        thrown.expect(NoSuchElementException.class);
        enumeratorStruct.next();
    }

    private static <T> void assertEqualSequences(Iterable<? extends T> left, Iterable<? extends T> right) {
        Object[] leftArray = Iterables.toArray(left, Object.class);
        Object[] rightArray = Iterables.toArray(right, Object.class);
        Assert.assertArrayEquals(leftArray, rightArray);
    }

    private <T> void enQueueDeQueueTestHelper(T... items) {
        assert items != null;

        ImmutableQueue<T> queue = ImmutableQueue.empty();
        int i = 0;
        for (T item : items) {
            ImmutableQueue<T> nextQueue = queue.enQueue(item);
            Assert.assertNotSame(queue, nextQueue);
            Assert.assertEquals(i, Iterables.size(queue));
            Assert.assertEquals(++i, Iterables.size(nextQueue));
            queue = nextQueue;
        }

        i = 0;
        for (T element : queue) {
            Assert.assertSame(items[i++], element);
        }

        i = 0;
        for (T element : queue) {
            Assert.assertSame(items[i++], element);
        }

        i = items.length;
        for (T expectedItem : items) {
            T actualItem = queue.head();
            Assert.assertSame(expectedItem, actualItem);
            ImmutableQueue<T> nextQueue = queue.deQueue();
            Assert.assertNotSame(queue, nextQueue);
            Assert.assertEquals(i, Iterables.size(queue));
            Assert.assertEquals(--i, Iterables.size(nextQueue));
            queue = nextQueue;
        }
    }

}