package com.demo.threads;

public class ThreadSyncDemo {

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

class MyWorker implements Runnable {


    private int N;
    private boolean isOdd;
    private Object lock;

    public MyWorker(int N, boolean isOdd, Object lock) {
        this.N = N;
        this.isOdd = isOdd;
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
        int i = 1;
        synchronized (lock){
            while(i <= N){

                if(i % 2 == 0 ){
                    System.out.println("even worker="+i);
                }
                i++;
                lock.notifyAll();

                try {
                    if(i > N){
                        break;
                    }
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }

    }


    private void oddWorker() {
        int i = 1;
        synchronized (lock){
            while(i <= N){

                if(i % 2 != 0 ){
                    System.out.println("odd worker="+i);
                }
                i++;
                lock.notifyAll();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}

