package org.Akorad.ComplexTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class ComplexTaskExecutor {
    private final int numTasks;

    public ComplexTaskExecutor(int numTasks) {
        this.numTasks = numTasks;
    }

    public void executeTasks(int numTasks) {
        if (numTasks <= 0) {
            throw new IllegalArgumentException("Количество задач должно быть больше нуля");
        }
        ExecutorService executor = newFixedThreadPool(numTasks);
        ConcurrentLinkedQueue<Integer> taskResults = new ConcurrentLinkedQueue<>();
        AtomicInteger aggregatedResult = new AtomicInteger(0);

        CyclicBarrier barrier = new CyclicBarrier(numTasks, () -> {
            int sum = taskResults.stream().mapToInt(Integer::intValue).sum();
            aggregatedResult.set(sum);
        });

        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            Callable<Integer> task = getIntegerCallable(i, taskResults, barrier);
            futures.add(executor.submit(task));
        }
        try {
            for (Future<Integer> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        int finalResult = aggregatedResult.get();
        System.out.println("Финальный результат для " + Thread.currentThread().getName() + " всех задач: " + finalResult);
    }

    private static Callable<Integer> getIntegerCallable(int i, ConcurrentLinkedQueue<Integer> taskResults, CyclicBarrier barrier) {
        final int taskId = i + 1;
        return () -> {
            try {
                ComplexTask complexTask = new ComplexTask(taskId);
                Integer result = complexTask.execute();
                taskResults.add(result);
                barrier.await();
                return result;
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        };
    }
}
