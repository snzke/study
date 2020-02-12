package org.snzke.lock;

import org.snzke.lock.blocking.CLHBlockingLock;

import java.text.SimpleDateFormat;
import java.util.Date;
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (int i = 0; i < 10; i++) {
                try{
                    lock.lock();
                    System.out.println(String.format("%s count[%s] time[%s]", threadName, i, sdf.format(new Date())));
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
//        Lock lock = new MCSLock();
        Lock lock = new CLHBlockingLock();
        for (int i = 0; i < 3; i++) {
            new Timer(lock).start();
        }
    }
}
