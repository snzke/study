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

    /**
     * 是否为末尾节点（无子节点）
     * @return 布尔值
     */
    public boolean getIsLast(){
        return null == left && null == right;
    }

    /**
     * 是否为叶子节点（拥有左右子节点）
     * @return 布尔值
     */
    public boolean getIsLeaf(){
        return null != left && null != right;
    }

    /**
     * 是否仅包含左叶子节点
     * @return 布尔值
     */
    public boolean onlyLeftLeaf(){
        return null != left && null == right;
    }

    /**
     * 是否仅包含右叶子节点
     * @return 布尔值
     */
    public boolean onlyRightLeaf(){
        return null == left && null != right;
    }

    /**
     * 当前叶子是否为父级的左叶子节点
     * @return 布尔值
     */
    public boolean getIsLeftLeaf(){
        return null != parent && this.equals(parent.getLeft());
    }

    /**
     * 当前叶子是否为父级的右叶子节点
     * @return 布尔值
     */
    public boolean getIsRightLeaf(){
        return null != parent && this.equals(parent.getRight());
    }

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
