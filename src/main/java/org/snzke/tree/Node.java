package org.snzke.tree;

/**
 * 树节点
 * @param <K> Key 需要实现 Comparable 接口
 * @param <V> Value 任意类型
 * @author snzke
 */
public class Node<K extends Comparable<K>, V> {
    private Node<K, V> left;
    private Node<K, V> right;
    private Node<K, V> parent;
    private Entry<K, V> entry;

    public Node<K, V> getLeft() {
        return left;
    }

    public void setLeft(Node<K, V> left) {
        this.left = left;
    }

    public Node<K, V> getRight() {
        return right;
    }

    public void setRight(Node<K, V> right) {
        this.right = right;
    }

    public Entry<K, V> getEntry() {
        return entry;
    }

    public void setEntry(Entry<K, V> entry) {
        if(entry == null){ throw new IllegalArgumentException("value must be not null"); }
        this.entry = entry;
    }

    public Node<K, V> getParent() {
        return parent;
    }

    public void setParent(Node<K, V> parent) {
        this.parent = parent;
    }

    public Node(Node<K, V> left, Node<K, V> right, Node<K, V> parent, Entry<K, V> entry) {
        super();
        if(entry == null){ throw new IllegalArgumentException("value must be not null"); }
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.entry = entry;
    }
}
