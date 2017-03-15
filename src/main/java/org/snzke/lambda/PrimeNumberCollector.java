package org.snzke.lambda;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.IntStream;

/**
 * 分组质数收集器
 */
public class PrimeNumberCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>>{

    /**
     * 根据谓词截断集合
     * @param list 需要截断的集合
     * @param p 谓词实例（一个需要包含返回布尔类型方法的匿名实例）
     * @param <A> 集合实例泛型
     * @return
     */
    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p){
        int i = 0;
        for (A item : list) {
            if (!p.test(item)){
                return list.subList(0, i);
            }
            i++;
        }
        return list;
    }

    /**
     * 判断candidate是否为质数<br/>
     * 逻辑：<br/>
     * &nbsp;　　依次循环 2 到 candidate的平方根    --> IntStream.rangeClosed(2, candidateRoot).boxed()<br/>
     * &nbsp;　　并判断中间的数字是否可整除candidate --> i -> candidate % i == 0<br/>
     * &nbsp;　　如果都不能够整除，则为质数          --> noneMatch<br/>
     * @param candidate 需要判断的数字
     * @return
     */
    public static boolean isPrime(int candidate){
        int candidateRoot = (int) Math.sqrt(candidate);
        // 依次循环 2 到 candidate 的平方根，并判断 candidate 是否为质数
        return IntStream.rangeClosed(2, candidateRoot).boxed().noneMatch(i -> candidate % i == 0);
    }

    /**
     * 根据质数集合判断candidate是否为质数
     * @param primeNumberList 小于candidate的质数集合
     * @param candidate 需要判断的数字
     * @return
     */
    public static boolean isPrime(List<Integer> primeNumberList, int candidate){
        int candidateRoot = (int) Math.sqrt(candidate);
        // 仅循环质数集合中的数 到 candidate 的平方根，并判断 candidate 是否为质数
        return takeWhile(primeNumberList, i -> i <= candidateRoot).stream().noneMatch(p -> candidate % p == 0);
    }

    /**
     * 收集器初始化方法，返回一个泛型为收集器器第二泛型的构造器
     *
     * 此处逻辑为返回一个带有质数标识的数字集合默认Map，为返回最终结果做准备
     * @return 一个泛型为收集器器第二泛型的构造器
     */
    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        // 双括号反模式
        // 以匿名内部类的方式创建一个实例
        // 该实例包含外部实例(Supplier)的引用
        // 有内存泄漏的风险
        return () -> new HashMap<Boolean, List<Integer>>(){{
            put(true, new ArrayList<>());
            put(false, new ArrayList<>());
        }};
    }

    /**
     * 收集器遍历流时调用的核心函数，返回一个收集器第二泛型的生产者实例
     *
     * 判断当前数字的质数标识，并放入到对应的分组质数Map中
     *
     * @return
     */
    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (primeNumberMap, number) -> primeNumberMap.get(isPrime(primeNumberMap.get(true), number)).add(number);
    }

    /**
     * 当characteristics函数返回的特性中包含CONCURRENT（支持多线程）时
     * 收集器会在多线程遍历完流后调用此函数将每个流结果整合
     *
     * 为兼容多线程方式实现，将两个结果整合为一个结果
     * 注：
     *     即便实现了此方法
     *     但依然不能兼容多线程方式
     *     因为该收集器核心算法是有顺序的
     *     故此方法永远不会被调用
     * @return 整合后的分组质数Map
     */
    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        // 更好的做法：throw new UnsupportedOperationException("不支持该操作！");

        // 返回一个整合实例
        return (leftPrimeNumberMap, rightPrimeNumberMap) -> {
            leftPrimeNumberMap.get(true).addAll(rightPrimeNumberMap.get(true));
            leftPrimeNumberMap.get(false).addAll(rightPrimeNumberMap.get(false));
            return leftPrimeNumberMap;
        };
    }

    /**
     * 便利完流后被最终调用的转换结果函数
     * 目的为将accumulator函数的结果（Collector接口的第二个泛型）整合为最终结果（Collector接口的第三个泛型）
     * 因本收集器的第二个泛型和第三个泛型一致
     * 故不需要进行转换
     * 返回Function提供的默认过渡函数实例
     * @return
     */
    @Override
    public Function finisher() {
        return Function.identity();
    }

    /**
     * 获取该收集器支持的特性
     *     CONCURRENT       accumulator可用于多线程环境
     *                      表示该收集器可以多线程方式调用combiner函数合并收集结果
     *                      如果收集器没有标为UNORDERED，则仅用于无序数据源(例：Set)时才可并行合并收集结果
     *
     *     UNORDERED        收集结果不受项目遍历和累计的顺序影响
     *                      注：当accumulator函数和finisher函数实现为无序时，可使用此特性
     *
     *     IDENTITY_FINISH  表示完成器(finisher函数)返回的是一个恒等函数(Function.identity())，可以跳过执行finisher函数
     * @return
     */
    @Override
    public Set<Characteristics> characteristics() {
        /*
            因该收集器收集逻辑算法中包含有序收集
            故无法使用 CONCURRENT 特性和 UNORDERED 特性
         */
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}
