package org.snzke.tree;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 抽象树
 * @param <K> Key 需要实现 Comparable 接口
 * @param <V> Value 任意类型
 * @author snzke
 */
public abstract class AbstractTree<K extends Comparable<K>, V> implements Tree<K, V> {
    protected Node<K, V> root = null;

    @Override
    public Node<K, V> getRoot() {
        return root;
    }

    @Override
    public void foreach(Consumer<V> consumer){
        foreach(root, consumer);
    }

    @Override
    public void foreach(BiConsumer<K, V> consumer){
        foreach(root, consumer);
    }

    private void foreach(Node<K, V> node, Consumer<V> consumer){
        if (node != null && node.getEntry() != null) {
            foreach(node.getLeft(), consumer);
            consumer.accept(node.getEntry().getValue());
            foreach(node.getRight(), consumer);
        }
    }

    private void foreach(Node<K, V> node, BiConsumer<K, V> consumer){
        if (node != null && node.getEntry() != null) {
            foreach(node.getLeft(), consumer);
            consumer.accept(node.getEntry().getKey(), node.getEntry().getValue());
            foreach(node.getRight(), consumer);
        }
    }
}
