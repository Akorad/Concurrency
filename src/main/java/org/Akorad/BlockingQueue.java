package org.Akorad;

public class BlockingQueue<T> {

    private final Object lock = new Object();
    private final T[] queue;
    private int head = 0;
    private int tail = 0;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public BlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Размер очереди должен быть больше нуля");
        }
        queue = (T[]) new Object[capacity];
    }

    public void enqueue(T item) throws InterruptedException {
        synchronized (lock) {
            while (size == queue.length) {
                lock.wait();
            }
            queue[tail] = item;
            tail = (tail + 1) % queue.length;
            size++;
            lock.notifyAll();
        }
    }

    public T dequeue() throws InterruptedException {
        synchronized (lock) {
            while (size == 0) {
                lock.wait();
            }
            T item = queue[head];
            head = (head + 1) % queue.length;
            size--;
            lock.notifyAll();
            return item;
        }
    }

    public int size() {
        synchronized (lock) {
            return size;
        }
    }
}
