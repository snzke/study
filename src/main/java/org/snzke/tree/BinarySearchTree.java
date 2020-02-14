package org.snzke.tree;

/**
 * 二叉查找树
 * @author snzke
 */
public class BinarySearchTree<K extends Comparable<K>, V> extends AbstractTree<K, V> implements Tree<K, V> {

    @Override
    public void insert(K key, V value) {
        insertNode(new Node<>(null, null, null, new Entry<>(key, value)));
    }

    private void insertNode(Node<K, V> node) {
        Node<K, V> parent = null;
        Node<K, V> crtNode = this.root;
        K nodeKey = node.getEntry().getKey();
        while (crtNode != null) {
            parent = crtNode;
            K key = crtNode.getEntry().getKey();
            if (key.compareTo(nodeKey) > 0) {
                crtNode = crtNode.getLeft();
            } else if (key.compareTo(nodeKey) < 0) {
                crtNode = crtNode.getRight();
            } else {
                throw new IllegalArgumentException("value is existed");
            }
        }
        if (parent != null) {
            K parentKey = parent.getEntry().getKey();
            if (nodeKey.compareTo(parentKey) < 0) {
                parent.setLeft(node);
            } else {
                parent.setRight(node);
            }
            node.setParent(parent);
        } else {
            this.root = node;
        }
    }

    @Override
    public V find(K key) {
        if(key == null){ throw new IllegalArgumentException("key must be not null"); }
        Node<K, V> node = root;
        while (node != null && node.getEntry().getKey().compareTo(key) != 0) {
            K nodeKey = node.getEntry().getKey();
            if (nodeKey.compareTo(key) > 0) {
                node = node.getLeft();
            } else if (nodeKey.compareTo(key) < 0) {
                node = node.getRight();
            }
        }
        return null != node ? node.getEntry().getValue() : null;
    }

    public Node<K, V> findNode(K key) {
        if(key == null){ throw new IllegalArgumentException("key must be not null"); }
        Node<K, V> node = root;
        while (node != null && node.getEntry().getKey().compareTo(key) != 0) {
            K nodeKey = node.getEntry().getKey();
            if (nodeKey.compareTo(key) > 0) {
                node = node.getLeft();
            } else if (nodeKey.compareTo(key) < 0) {
                node = node.getRight();
            }
        }
        return node;
    }

    @Override
    public void delete(K key) {
        Node<K, V> node = findNode(key);
        if (node != null) {
            Node<K, V> parent = node.getParent();
            if(null == parent){
                root = null;
            }else{
                // 是否为叶子节点
                boolean leaf = node.getIsLeaf();
                // 是否仅拥有左叶子
                boolean onlyLeftLeaf = node.onlyLeftLeaf();
                // 是否为父级的左叶子
                boolean isLeftLeaf = node.getIsLeftLeaf();

                Node<K, V> waitReplace;
                if(onlyLeftLeaf){
                    waitReplace = node.getLeft();
                    node.getLeft().setParent(null);
                }else{
                    waitReplace = node.getRight();
                    node.getRight().setParent(null);
                }
                if(isLeftLeaf){
                    if(leaf){
                        Node<K, V> farLeft = node.getRight().getLeft();
                        while(null != farLeft && null != farLeft.getLeft()){
                            farLeft = farLeft.getLeft();
                        }
                        // 将被删除节点左叶子沉底到右叶子树中（沉底）
                        node.getLeft().setParent(farLeft);
                    }
                    parent.setLeft(waitReplace);
                }else{
                    if(leaf){
                        Node<K, V> farRight = node.getLeft().getRight();
                        while(null != farRight && null != farRight.getRight()){
                            farRight = farRight.getRight();
                        }
                        // 将被删除节点左叶子沉底到右叶子树中（沉底）
                        node.getRight().setParent(farRight);
                    }
                    parent.setRight(waitReplace);
                }

            }
        } else {
            throw new IllegalArgumentException("value is not exists");
        }
    }

}
