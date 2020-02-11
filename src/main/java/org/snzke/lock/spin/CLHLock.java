package org.snzke.lock.spin;

import org.snzke.lock.Lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 本地变量链表自旋锁
 * 优点：
 *  1.可扩展
 *  2.高性能
 *  3.获取锁顺序公平
 * 缺点：
 *  1.不适合在NUMA 架构下使用
 * @author Craig，Landin and Hagersten
 */
public class CLHLock implements Lock {
    public static class CLHNode {
        private volatile boolean isLocked = true;
    }

    private volatile CLHNode tail;
    private static final ThreadLocal<CLHNode> LOCAL = new ThreadLocal<>();
    private static final AtomicReferenceFieldUpdater<CLHLock, CLHNode> UPDATER = AtomicReferenceFieldUpdater.newUpdater(CLHLock.class,
            CLHNode.class, "tail");

    @Override
    public void lock() {
        CLHNode node = new CLHNode();
        LOCAL.set(node);
        CLHNode preNode = UPDATER.getAndSet(this, node);
        if (preNode != null) {
            while (preNode.isLocked) {
            }
            preNode = null;
            LOCAL.set(node);
        }
    }

    @Override
    public void unlock() {
        CLHNode node = LOCAL.get();
        // 如果compareAndSet返回true，说明当前没有线程竞争锁
        if (!UPDATER.compareAndSet(this, node, null)) {
            node.isLocked = false;
        }
        node = null;
    }
}
