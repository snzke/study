package org.snzke.lock.blocking;

import org.snzke.lock.Lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;

/**
 * 基于CLH自旋锁实现的阻塞锁
 *
 * 本地变量链表自旋锁
 * 优点：
 *  1.可扩展
 *  2.高性能
 *  3.获取锁顺序公平
 *  4.不会浪费CPU占用率
 *
 * 在线程竞争不激烈的情况下，使用自旋锁。线程竞争激烈的情况下，使用阻塞锁
 *
 * @author snzke
 */
public class CLHBlockingLock implements Lock {
    public static class CLHBlockingNode {
        private volatile Thread isLocked;
    }

    private volatile CLHBlockingNode tail;
    private static final ThreadLocal<CLHBlockingNode> LOCAL = new ThreadLocal<>();
    private static final AtomicReferenceFieldUpdater<CLHBlockingLock, CLHBlockingNode> UPDATER = AtomicReferenceFieldUpdater.newUpdater(CLHBlockingLock.class,
            CLHBlockingNode.class, "tail");

    @Override
    public void lock() {
        CLHBlockingNode node = new CLHBlockingNode();
        LOCAL.set(node);
        CLHBlockingNode preNode = UPDATER.getAndSet(this, node);
        if (preNode != null) {
            preNode.isLocked = Thread.currentThread();
            LockSupport.park(this);
            LOCAL.set(node);
        }
    }

    @Override
    public void unlock() {
        CLHBlockingNode node = LOCAL.get();
        if (!UPDATER.compareAndSet(this, node, null)) {
            LockSupport.unpark(node.isLocked);
        }
    }
}
