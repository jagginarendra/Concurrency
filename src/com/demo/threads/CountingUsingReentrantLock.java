package com.demo.threads;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//Que Given a number N , print odd and even number using 2 different thread one by one.
public class CountingUsingReentrantLock {

    public static void main(String[] args) throws InterruptedException {

        int N = 10;
        ReentrantLock reentrantLock = new ReentrantLock(true);
        Condition evenCondition = reentrantLock.newCondition();
        Condition oddCondition = reentrantLock.newCondition();
        EvenOddWorker evenWorker = new EvenOddWorker(N,false, evenCondition,oddCondition,reentrantLock);
        EvenOddWorker oddWorker = new EvenOddWorker(N,true, evenCondition,oddCondition,reentrantLock);
        Thread oddThread = new Thread(oddWorker);
        Thread evenThread = new Thread(evenWorker);
        oddThread.start();
        evenThread.start();
        oddThread.join();
        evenThread.join();
        System.out.println("Main Exiting");
    }

}


class EvenOddWorker implements Runnable {

    private int N;
    private boolean isOdd;
    private Condition evenCondition;
    private Condition oddCondition;
    private ReentrantLock lock;

    public EvenOddWorker(int n, boolean isOdd, Condition evenCondition, Condition oddCondition, ReentrantLock lock) {
        N = n;
        this.isOdd = isOdd;
        this.evenCondition = evenCondition;
        this.oddCondition = oddCondition;
        this.lock = lock;
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
        try {
            lock.lock();
            int i = 1;
            while (i <= N) {

                if (i % 2 == 0) {
                    System.out.println("even=" + i);
                }
                i++;
                oddCondition.signal(); // notify
                if (i <= N)
                    evenCondition.await(); // wait
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void oddWorker() {
        try {
            lock.lock();
            int i = 1;
            while (i <= N) {

                if (i % 2 != 0) {
                    System.out.println("odd=" + i);
                }
                i++;
                evenCondition.signal(); // notify
                if (i <= N)
                    oddCondition.await(); // wait
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}

