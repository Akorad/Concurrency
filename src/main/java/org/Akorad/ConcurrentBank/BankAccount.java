package org.Akorad.ConcurrentBank;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private BigDecimal balance;
    private final int accountId;
    private final ReentrantLock lock = new ReentrantLock();


    public BankAccount(int accountId, BigDecimal initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма депозита должна быть больше нуля");
        }
        lock.lock();
        try {
            balance = balance.add(amount);
            System.out.println("Счёт " + accountId + ": внесено " + amount + ", новый баланс: " + balance);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть больше нуля");
        }
        lock.lock();
        try {
            if (balance.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Недостаточно средств на счёте " + accountId);
            }
            balance = balance.subtract(amount);
            System.out.println("Счёт " + accountId + ": снято " + amount + ", новый баланс: " + balance);
        } finally {
            lock.unlock();
        }
    }
    public BigDecimal getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public int getAccountId() {
        return accountId;
    }
    public void lock() {
        lock.lock();
    }
    public void unlock() {
        lock.unlock();
    }
}
