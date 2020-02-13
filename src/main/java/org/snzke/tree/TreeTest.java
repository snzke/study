package org.snzke.tree;

import org.snzke.lambda.Dish;

public class TreeTest {
    public static void main(String[] args) {
        Tree<String, Dish> bt = new BinarySearchTree<>();

        Dish.MENU.forEach(dish -> bt.insert(new Entry<>(dish.getName(), dish)));
        bt.foreach(System.out::println);
        System.out.println("----------------------");
        bt.delete("宫保鸡丁");
        bt.foreach(System.out::println);
    }
}
