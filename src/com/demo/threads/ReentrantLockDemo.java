package com.demo.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    public static void main(String[] args) throws InterruptedException {

        int N = 10;

        ReentrantLock lock = new ReentrantLock();
        Condition evenCondition = lock.newCondition();
        Condition oddCondition = lock.newCondition();

        CounterWorker evenWorker = new CounterWorker(N, lock, evenCondition, oddCondition, false);
        CounterWorker oddWorker = new CounterWorker(N, lock, evenCondition, oddCondition, true);

        Thread t1 = new Thread(evenWorker);
        Thread t2 = new Thread(oddWorker);
        t2.start();
        Thread.sleep(100);
        t1.start();

        System.out.println("Main exiting");
    }

}

class CounterWorker implements Runnable {

    private int N;
    private ReentrantLock lock;
    private Condition evenCondition;
    private Condition oddCondition;
    private boolean isOdd;


    public CounterWorker(int n, ReentrantLock lock, Condition evenCondition, Condition oddCondition, boolean isOdd) {
        N = n;
        this.lock = lock;
        this.evenCondition = evenCondition;
        this.oddCondition = oddCondition;
        this.isOdd = isOdd;
    }


    @Override
    public void run() {

        if (isOdd) {
            oddWorker();
        } else {
            evenWorker();
        }

    }

    private void evenWorker() {

        int i = 1;
        while (i <= N) {
            try {
                lock.lock();
                if (i % 2 == 0) {
                    System.out.println("even=" + i);
                }
                i++;
                oddCondition.signal();
                if (i <= N)
                    evenCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private void oddWorker() {
        int i = 1;
        while (i <= N) {
            try {
                lock.lock();
                if (i % 2 != 0) {
                    System.out.println("odd=" + i);
                }
                i++;
                evenCondition.signal();
                if (i <= N)
                    oddCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }
}
