package org.snzke.lock.spin;

import org.snzke.lock.Lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 常规自旋锁
 * 优点：
 *  1.高性能
 * 缺点：
 *  1.当持有锁时间过长时会造成CPU使用率极高
 *  2.获取锁顺序不公平
 * @author snzke
 */
public class SpinLock implements Lock {
    private AtomicReference<Thread> sign = new AtomicReference<>();

    @Override
    public void lock() {
        Thread current = Thread.currentThread();

        // CAS自旋
        while (!sign.compareAndSet(null, current)) {
        }
    }

    @Override
    public void unlock() {
        Thread current = Thread.currentThread();
        sign.compareAndSet(current, null);
    }
}
