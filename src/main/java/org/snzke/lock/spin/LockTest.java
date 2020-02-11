package org.snzke.lock.spin;

import org.snzke.lock.Lock;

import java.util.concurrent.TimeUnit;

public class LockTest {

    static class Timer extends Thread{
        private Lock lock;

        public Timer(Lock lock){
            this.lock = lock;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();

            for (int i = 0; i < 10; i++) {
                try{
                    lock.lock();
                    System.out.println(threadName + " Time[" + i + "]:" + System.currentTimeMillis());
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
    public static void main(String[] args) {
//        Lock lock = new SpinLock();
//        Lock lock = new TicketLock();
//        Lock lock = new CLHLock();
        Lock lock = new MCSLock();
        for (int i = 0; i < 3; i++) {
            new Timer(lock).start();
        }
    }
}
