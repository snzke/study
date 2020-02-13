package org.snzke.tree;

/**
 * 树节点元素
 * @param <K> Key
 * @param <V> Value
 * @author snzke
 */
public class Entry<K extends Comparable<? extends K>, V> {
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

}
