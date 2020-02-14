package org.snzke.tree;

import org.snzke.lambda.Dish;

public class TreeTest {
    public static void main(String[] args) {
//        Tree<String, Dish> bt = new BinarySearchTree<>();
//
//        Dish.MENU.forEach(dish -> bt.insert(dish.getName(), dish));
//        bt.foreach(System.out::println);
//        System.out.println("----------------------");
//        bt.delete("宫保鸡丁");
//        bt.foreach(System.out::println);

        int [] a = {3, 2, 1, 4, 5, 6, 7, 10, 9, 8};

        Tree<Integer, Integer> bat = new BinaryAvlTree<>();
        for (int i = 0; i < a.length; i++) {
            bat.insert(a[i], a[i]);
        }

        bat.foreach(System.out::println);
        System.out.println("----------------------");

        bat.delete(7);

        bat.foreach(System.out::println);
    }
}
