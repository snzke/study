package org.snzke.lambda;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;
import static org.snzke.lambda.Dish.menu;
import static org.snzke.lambda.Dish.CaloricLevel;

/**
 * Created by snzke on 2017/3/12.
 */
public class CollectorsDemo {
    public static void groupingByDemo(){
        System.out.println("菜单总数：" + menu.stream().count());
        System.out.println("菜单热量总和：" + menu.stream().collect(summingInt(Dish::getCalories)));
        System.out.println("热量最高的菜：" + menu.stream().collect(maxBy(Comparator.comparingInt(Dish::getCalories))).get());
        System.out.println("拼接菜单名称：" + menu.stream().map(Dish::getName).collect(joining(", ")));
        System.out.println("根据菜单类型分组：" + menu.stream().collect(groupingBy(Dish::getType)));
        System.out.println("根据类型中热量最大菜单分组：" + menu.stream().collect(groupingBy(Dish::getType, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get))));
        System.out.println("根据热量自定义分组：" + menu.stream().collect(groupingBy((Dish dish) -> {
            if(dish.getCalories() > 700) return CaloricLevel.高脂肪;
            else if(dish.getCalories() > 400) return CaloricLevel.普通;
            else return CaloricLevel.健康;
        })));
        System.out.println("根据热量自定义分组：" + menu.stream().collect(groupingBy(Dish::getCaloricLevel)));
        System.out.println("根据类型和热量二级分组：" + menu.stream().collect(groupingBy(Dish::getType, groupingBy(Dish::getCaloricLevel))));
        System.out.println("根据类型计算热量总和：" + menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories))));
        System.out.println("根据类型统计热量级别<Set>" + menu.stream().collect(groupingBy(Dish::getType, mapping(Dish::getCaloricLevel, toSet()))));
        System.out.println("根据类型统计热量级别<HashSet>：" + menu.stream().collect(groupingBy(Dish::getType, mapping(Dish::getCaloricLevel, toCollection(HashSet::new)))));
        System.out.println("根据素菜标识分组：" + menu.stream().collect(partitioningBy(Dish::isVegetarian)));
        System.out.println("根据素菜标识和类型进行二级分组：" + menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType))));
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
            IntStream.rangeClosed(2, 100_0000).boxed().collect(partitioningBy(PrimeNumberCollector::isPrime));
            // 优化算法
//            IntStream.rangeClosed(2, 100_0000).boxed().collect(new PrimeNumberCollector());
            long duration = (System.nanoTime() - start) / 100_0000;
            System.out.println("耗时：" + duration);
            if(duration < fastestTime){
                fastestTime = duration;
            }
        }
        System.out.println("最快毫秒数：" + fastestTime);
    }
}
