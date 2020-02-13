package org.snzke.tree;

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
    public void foreach(Consumer<Entry<K, V>> consumer){
        foreach0(root, consumer);
    }

    private void foreach0(Node<K, V> node, Consumer<Entry<K, V>> consumer){
        if (node != null && node.getEntry() != null) {
            foreach0(node.getLeft(), consumer);
            consumer.accept(node.getEntry());
            foreach0(node.getRight(), consumer);
        }
    }
}
