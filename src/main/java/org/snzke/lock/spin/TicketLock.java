package org.snzke.lock.spin;

import org.snzke.lock.Lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 排队自旋锁
 * 优点：
 *  1.不会使切换线程状态
 *  2.执行速度快
 *  3.按顺序获取锁
 * 缺点：
 *  1.当持有锁时间过长时会造成CPU使用率极高
 * @author snzke
 */
public class TicketLock implements Lock {
    private AtomicInteger serviceNum = new AtomicInteger();
    private AtomicInteger ticketNum = new AtomicInteger();
    private static final ThreadLocal<Integer> LOCAL = new ThreadLocal<Integer>();

    @Override
    public void lock() {
        int ticket = ticketNum.getAndIncrement();
        LOCAL.set(ticket);
        while (ticket != serviceNum.get()) {
        }

    }

    @Override
    public void unlock() {
        int ticket = LOCAL.get();
        serviceNum.compareAndSet(ticket, ticket + 1);
    }
}
