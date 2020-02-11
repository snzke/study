package org.snzke.lock.spin;

import org.snzke.lock.Lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 可重入自旋锁
 * 优点：
 *  1.高性能
 * 缺点：
 *  1.当持有锁时间过长时会造成CPU使用率极高
 *  2.获取锁顺序不公平
 * @author snzke
 */
public class ReentrantSpinLock implements Lock {
    private AtomicReference<Thread> cas = new AtomicReference<>();
    private int count;

    @Override
    public void lock() {
        Thread current = Thread.currentThread();
        // 如果当前线程已经获取到了锁，线程数增加一，返回
        if (current == cas.get()) {
            count++;
            return;
        }
        // 如果没获取到锁，则通过CAS自旋
        while (!cas.compareAndSet(null, current)) {
        }
    }

    @Override
    public void unlock() {
        Thread cur = Thread.currentThread();
        if (cur == cas.get()) {
            // 如果大于0，表示当前线程多次获取了该锁，释放锁通过count减一来模拟
            if (count > 0) {
                count--;
            // 如果count==0，可以将锁释放，这样就能保证获取锁的次数与释放锁的次数是一致的了。
            } else {
                cas.compareAndSet(cur, null);
            }
        }
    }
}