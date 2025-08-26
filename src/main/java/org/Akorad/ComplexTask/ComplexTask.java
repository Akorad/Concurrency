package org.Akorad.ComplexTask;

public class ComplexTask {
    private final int id;

    public ComplexTask(int id) {
        this.id = id;
    }
    public Integer execute() {
        try {
            // симуляция тяжёлой работы
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // сигнализируем об остановке
            return 0;
        }
        int result = id * id; //результат задачи
        System.out.println(Thread.currentThread().getName() + " выполняет задачу " + id + " -> " + result);
        return result;
    }
}
