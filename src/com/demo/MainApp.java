package com.demo;

public class MainApp {

    public static void main(String[] args) {

        int N = 10;
        Object lock = new Object();
        MyWorker oddworker = new MyWorker(N, true, lock);
        MyWorker evenworker = new MyWorker(N, false, lock);
        Thread oddThread = new Thread(oddworker);
        Thread evenThread = new Thread(evenworker);
        oddThread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        evenThread.start();
        System.out.println("Main exiting");
    }

}
