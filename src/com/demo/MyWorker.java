package com.demo;

public class MyWorker implements Runnable {


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
