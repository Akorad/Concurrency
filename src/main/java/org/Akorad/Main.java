package org.Akorad;


public class Main {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new BlockingQueue<>(1);

        Runnable producer = () -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    System.out.println("Производитель " + Thread.currentThread().getName() +" добавил: " + i + " яблоко");
                    queue.enqueue(i);
                    Thread.sleep(100); // Симуляция времени производства
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = () -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    int item = queue.dequeue();
                    System.out.println("Потребитель " + Thread.currentThread().getName()+" взял: " + item + " яблоко");
                    Thread.sleep(150); // Симуляция времени потребления
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        new Thread(producer, "Producer-Gena").start();
        new Thread(consumer, "Consumer-Mischa").start();
        new Thread(producer, "Producer-Sasha").start();
        new Thread(consumer, "Consumer-Dima").start();
    }
}