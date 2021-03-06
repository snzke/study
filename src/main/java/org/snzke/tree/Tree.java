package org.snzke.tree;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 容器 - 树
 * @param <K> Key 需要实现 Comparable 接口
 * @param <V> Value 任意类型
 * @author snzke
 */
public interface Tree<K extends Comparable<K>, V> {
    /**
     * 插入元素
     * @param key key
     * @param value value
     */
    void insert(K key, V value);

    /**
     * 根据Key 删除元素
     * @param key Key
     */
    void delete(K key);

    /**
     * 根据Key 查找元素
     * @param key Key
     * @return Value
     */
    V find(K key);

    /**
     * 获取树的根元素
     * @return 根元素
     */
    Node<K, V> getRoot();

    /**
     * 遍历值
     * @param consumer 值消费者
     */
    void foreach(Consumer<V> consumer);

    /**
     * 遍历键值
     * @param consumer 键值消费者
     */
    void foreach(BiConsumer<K, V> consumer);
}
