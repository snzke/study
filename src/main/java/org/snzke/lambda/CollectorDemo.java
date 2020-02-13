package org.snzke.lambda;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;
import static org.snzke.lambda.Dish.CaloricLevel;
import static org.snzke.lambda.Dish.MENU;

/**
 * 收集器示例
 */
public class CollectorDemo {
    public static void groupingByDemo(){
        System.out.println("菜单总数：" + (long) MENU.size());
        System.out.println("菜单热量总和：" + (Integer) MENU.stream().mapToInt(Dish::getCalories).sum());
        System.out.println("热量最高的菜：" + MENU.stream().max(Comparator.comparingInt(Dish::getCalories)).get());
        System.out.println("拼接菜单名称：" + MENU.stream().map(Dish::getName).collect(joining(", ")));
        System.out.println("根据菜单类型分组：" + MENU.stream().collect(groupingBy(Dish::getType)));
        System.out.println("根据类型中热量最大菜单分组：" + MENU.stream().collect(toMap(Dish::getType, Function.identity(), BinaryOperator.maxBy(Comparator.comparingInt(Dish::getCalories)))));
        System.out.println("根据热量自定义分组：" + MENU.stream().collect(groupingBy((Dish dish) -> {
            if(dish.getCalories() > 700) { return CaloricLevel.高脂肪; }
            else if(dish.getCalories() > 400) { return CaloricLevel.普通; }
            else { return CaloricLevel.健康; }
        })));
        System.out.println("根据热量级别分组，并根据热量排序：" + MENU.stream().collect(groupingBy(Dish::getCaloricLevel, Collectors.collectingAndThen(toList(), (list) -> {
            list.sort(Comparator.comparingInt(Dish::getCalories));
            return list;
        }))));
        System.out.println("根据类型和热量二级分组：" + MENU.stream().collect(groupingBy(Dish::getType, groupingBy(Dish::getCaloricLevel))));
        System.out.println("根据类型计算热量总和：" + MENU.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories))));
        System.out.println("根据类型统计热量级别<Set>" + MENU.stream().collect(groupingBy(Dish::getType, mapping(Dish::getCaloricLevel, toSet()))));
        System.out.println("根据类型统计热量级别<HashSet>：" + MENU.stream().collect(groupingBy(Dish::getType, mapping(Dish::getCaloricLevel, toCollection(HashSet::new)))));
        System.out.println("根据素菜标识分组：" + MENU.stream().collect(partitioningBy(Dish::isVegetarian)));
        System.out.println("根据素菜标识和类型进行二级分组：" + MENU.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType))));
    }
    public static void main(String [] args){
        groupingByDemo();
        performanceTesting();
    }
    public static void performanceTesting(){
        System.out.println("2到10的质数分区：" + IntStream.rangeClosed(2, 10).boxed().collect(partitioningBy(PrimeNumberCollector::isPrime)));
        System.out.println("2到10的质数分区：" + IntStream.rangeClosed(2, 10).boxed().collect(new PrimeNumberCollector()));
        long fastestTime = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            // 常规算法
//            IntStream.rangeClosed(2, 100_0000).boxed().collect(partitioningBy(PrimeNumberCollector::isPrime));
            // 优化算法
            IntStream.rangeClosed(2, 100_0000).boxed().collect(new PrimeNumberCollector());
            long duration = (System.nanoTime() - start) / 100_0000;
            System.out.println("耗时：" + duration);
            if(duration < fastestTime){
                fastestTime = duration;
            }
        }
        System.out.println("最快毫秒数：" + fastestTime);
    }
}
