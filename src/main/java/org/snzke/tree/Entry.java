package org.snzke.tree;

/**
 * 树节点元素
 * @param <K> Key 需要实现 Comparable 接口
 * @param <V> Value 任意类型
 * @author snzke
 */
public class Entry<K extends Comparable<K>, V> {
    private K key;
    private V value;

    public Entry(K key, V value) {
        if(null == key){ throw new IllegalArgumentException("key must be not null"); }
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return null != value ? value.toString() : null;
    }
}
