package org.snzke.tree;

import org.snzke.lambda.Dish;

public class TreeTest {
    public static void main(String[] args) {
        BinarySearchTree bt = new BinarySearchTree();

        Dish.MENU.forEach(bt::insert);
        bt.foreach(System.out::println);
        System.out.println("----------------------");
        bt.delete(Dish.MENU.get(2));
        bt.foreach(System.out::println);
    }
}
