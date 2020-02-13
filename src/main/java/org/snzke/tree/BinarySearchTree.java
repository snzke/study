package org.snzke.tree;

/**
 * 二叉查找树
 * @author snzke
 */
public class BinarySearchTree<K extends Comparable<K>, V> extends AbstractTree<K, V> implements Tree<K, V> {

    @Override
    public void insert(Entry<K, V> entry) {
        insertNode(new Node<>(null, null, null, entry));
    }

    private void insertNode(Node<K, V> node) {
        Node<K, V> pre = null;
        Node<K, V> x = this.root;
        K nodeKey = node.getEntry().getKey();
        while (x != null) {
            pre = x;
            K xKey = x.getEntry().getKey();
            if (xKey.compareTo(nodeKey) > 0) {
                x = x.getLeft();
            } else if (xKey.compareTo(nodeKey) < 0) {
                x = x.getRight();
            } else {
                throw new IllegalArgumentException("value is existed");
            }
        }
        if (pre != null) {
            K preKey = pre.getEntry().getKey();
            if (nodeKey.compareTo(preKey) < 0) {
                pre.setLeft(node);
            } else {
                pre.setRight(node);
            }
            node.setParent(pre);
        } else {
            this.root = node;
        }
    }

    @Override
    public V find(K key) {
        if(key == null){ throw new IllegalArgumentException("key must be not null"); }
        Node<K, V> x = root;
        while (x != null && x.getEntry().getKey().compareTo(key) != 0) {
            K xKey = x.getEntry().getKey();
            if (xKey.compareTo(key) > 0) {
                x = x.getLeft();
            } else if (xKey.compareTo(key) < 0) {
                x = x.getRight();
            }
        }
        return null != x ? x.getEntry().getValue() : null;
    }

    public Node<K, V> findNode(K key) {
        if(key == null){ throw new IllegalArgumentException("key must be not null"); }
        Node<K, V> x = root;
        while (x != null && x.getEntry().getKey().compareTo(key) != 0) {
            K xKey = x.getEntry().getKey();
            if (xKey.compareTo(key) > 0) {
                x = x.getLeft();
            } else if (xKey.compareTo(key) < 0) {
                x = x.getRight();
            }
        }
        return x;
    }

    @Override
    public void delete(K key) {
        Node<K, V> x = findNode(key);
        if (x != null) {
            if (x.getLeft() == null && x.getRight() == null) {
                x.setLeft(null);
                x.setRight(null);
            } else if (x.getLeft() != null) {
                Node<K, V> y = x.getLeft();
                while (y.getRight() != null) {
                    y = y.getRight();
                }
                x.setEntry(y.getEntry());
                y.getParent().setRight(null);
            } else {
                Node<K, V> y = x.getRight();
                if (x.getParent() != null) {
                    y.setLeft(x.getLeft());
                    if (x.getParent().getEntry().getKey().compareTo(x.getEntry().getKey()) > 0) {
                        x.getParent().setLeft(y);
                    } else {
                        x.getParent().setRight(y);
                    }
                } else {
                    this.root = y;
                }
            }
        } else {
            throw new IllegalArgumentException("value is not exists");
        }
    }

}
