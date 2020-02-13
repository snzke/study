package org.snzke.tree;

import java.util.function.Consumer;

/**
 * 二叉查找树
 * @author snzke
 */
public class BinarySearchTree {
    private static class Node {
        private Node left;
        private Node right;
        private Node parent;
        private Object value;

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            if(value == null){ throw new IllegalArgumentException("value must be not null"); }
            this.value = value;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node(Node left, Node right, Node parent, Object value) {
            super();
            if(value == null){ throw new IllegalArgumentException("value must be not null"); }
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.value = value;
        }

        public int valueHash(){
            return value.hashCode();
        }
    }

    private Node root = null;

    public void insert(Object value) {
        insertNode(new Node(null, null, null, value));
    }

    private void insertNode(Node node) {
        Node pre = null;
        Node x = this.root;
        int nc = node.valueHash();
        while (x != null) {
            pre = x;
            int xc = x.valueHash();
            if (xc > nc) {
                x = x.getLeft();
            } else if (xc < nc) {
                x = x.getRight();
            } else {
                throw new IllegalArgumentException("value is existed");
            }
        }
        if (pre != null) {
            int pc = pre.valueHash();
            if (nc < pc) {
                pre.setLeft(node);
            } else {
                pre.setRight(node);
            }
            node.setParent(pre);
        } else {
            this.root = node;
        }
    }

    public Node find(Object value) {
        if(value == null){ throw new IllegalArgumentException("value must be not null"); }
        Node x = root;
        int vc = value.hashCode();
        while (x != null && x.getValue() != value) {
            int xc = x.valueHash();
            if (xc > vc) {
                x = x.getLeft();
            } else if (xc < vc) {
                x = x.getRight();
            }
        }
        return x;
    }

    public void delete(Object value) {
        Node x = find(value);
        if (x != null) {
            if (x.getLeft() == null && x.getRight() == null) {
                x.setLeft(null);
                x.setRight(null);
            } else if (x.getLeft() != null) {
                Node y = x.getLeft();
                while (y.getRight() != null) {
                    y = y.getRight();
                }
                x.setValue(y.getValue());
                y.getParent().setRight(null);
            } else {
                Node y = x.getRight();
                if (x.getParent() != null) {
                    y.setLeft(x.getLeft());
                    if (x.getParent().valueHash() > x.valueHash()) {
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

    public Node getRoot() {
        return root;
    }

    public void foreach(Consumer<Object> consumer){
        foreach0(root, consumer);
    }

    private void foreach0(Node node, Consumer<Object> consumer){
        if (node != null && node.getValue() != null) {
            foreach0(node.getLeft(), consumer);
            consumer.accept(node.getValue());
            foreach0(node.getRight(), consumer);
        }
    }
}
