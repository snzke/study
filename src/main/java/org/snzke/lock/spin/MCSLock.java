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
 *  1.暂无
 * @author John Mellor-Crummey, Michael Scott
 */
public class MCSLock implements Lock {
    public static class MCSNode {
        volatile MCSNode next;
        volatile boolean isLocked = true;
    }

    private static final ThreadLocal<MCSNode> NODE = new ThreadLocal<>();
    private volatile MCSNode queue;
    private static final AtomicReferenceFieldUpdater<MCSLock, MCSNode> UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(MCSLock.class, MCSNode.class, "queue");

    @Override
    public void lock() {
        MCSNode currentNode = new MCSNode();
        NODE.set(currentNode);
        MCSNode preNode = UPDATER.getAndSet(this, currentNode);
        if (preNode != null) {
            preNode.next = currentNode;
            while (currentNode.isLocked) {
            }
        }
    }

    @Override
    public void unlock() {
        MCSNode currentNode = NODE.get();
        if (currentNode.next == null) {
            if (!UPDATER.compareAndSet(this, currentNode, null)) {
                while (currentNode.next == null) {
                }
            }
        } else {
            currentNode.next.isLocked = false;
            currentNode.next = null;
        }
    }
}