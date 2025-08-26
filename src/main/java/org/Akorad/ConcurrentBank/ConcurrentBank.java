package org.Akorad.ConcurrentBank;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentBank {
    private final Map<Integer, BankAccount> accounts = new HashMap<>();
    private int nextAccountId = 1;
    private final ReentrantLock bankLock = new ReentrantLock();

    public BankAccount createAccount(int initialBalance) {
        bankLock.lock();
        try {
            int accountId = nextAccountId++;
            BankAccount account = new BankAccount(accountId, BigDecimal.valueOf(initialBalance));
            accounts.put(accountId, account);
            return account;
        } finally {
            bankLock.unlock();
        }
    }
    public void transfer(BankAccount from, BankAccount to, int amount) {
        if (from.getAccountId() == to.getAccountId()) {
            throw new IllegalArgumentException("Нельзя перевести деньги на тот же счёт");
        }
        // Определение порядка блокировок для предотвращения взаимной блокировки
        BankAccount firstLock = from.getAccountId() < to.getAccountId() ? from : to;
        BankAccount secondLock = from.getAccountId() < to.getAccountId() ? to : from;

        firstLock.lock();
        secondLock.lock();
        try {
            from.withdraw(BigDecimal.valueOf(amount));
            to.deposit(BigDecimal.valueOf(amount));
            System.out.println("Переведено " + amount + " со счёта " + from.getAccountId() + " на счёт " + to.getAccountId());
        } finally {
            secondLock.unlock();
            firstLock.unlock();
        }
    }
    public BigDecimal getTotalBalance() {
        bankLock.lock();
        try {
            return accounts.values().stream()
                    .map(BankAccount::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } finally {
            bankLock.unlock();
        }
    }

}
