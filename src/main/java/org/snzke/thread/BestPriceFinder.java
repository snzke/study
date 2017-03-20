package org.snzke.thread;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 最佳价格查询器
 * CompletableFuture 示例
 */
public class BestPriceFinder {
    private static final Random random = new Random();
    /**
     * 自定义线程池
     * 定义线程最小数量为店铺数量
     * 定义线程最大数量为400
     * 线程池最大线程数量公式：
     * 线程池最大线程数 = CPU核心数 * 期望CPU利用率 * (1 + 程序等待时间百分比 / 程序执行时间百分比)
     *      400       =     4    *     100%     * (1 +       99%        /       1%       )
     *      400       =     4    *     100%     * (1 +      0.99        /     0.01       )
     */
    private final Executor executor = Executors.newFixedThreadPool(Math.min(shopList.size(), 400),
            r -> {
                Thread thread = new Thread(r);
                // 设置为守护线程
                thread.setDaemon(true);
                return thread;
            });
    public static List<Shop> shopList = Arrays.asList(
            new Shop("国美电器"),
            new Shop("苏宁易购"),
            new Shop("京东商城"),
            new Shop("天猫商城"),
            new Shop("淘宝商城"),
            new Shop("1号店"),
            new Shop("聚美优品"),
            new Shop("唯品会"),
            new Shop("小米商城"));

    /**
     * 多线程并行方式获取多家店铺价格
     * @param product
     * @return
     */
    public List<String> findPrices(String product){
        // Stream串行获取价格，速度最慢
//        return shopList.stream().map(shop -> String.format("%s 的价格是 %.2f", shop.getName(), getPrice(product))).collect(Collectors.toList());

        // Stream并行获取价格，速度快，自动根据CPU核心数建立线程池执行线程
//        return shopList.parallelStream().map(shop -> String.format("%s 的价格是 %.2f", shop.getName(), getPrice(product))).collect(Collectors.toList());

        // 使用CompletableFuture并行获取价格
//        List<CompletableFuture<String>> priceFutureList = shopList.stream().map(
//                shop -> CompletableFuture.supplyAsync(
//                        () -> String.format("%s 的价格是 %.2f", shop.getName(), getPrice(product))
////                ) // 未使用线程池，此方式较慢
//                , executor) // 为supplyAsync函数使用自定义线程池，此方式最理想
//        ).collect(Collectors.toList());
//        return priceFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());

        // 使用折扣服务串行方式获取价格
//        return shopList.stream()
//                .map(shop -> shop.getDiscountPrice(product))
//                .map(Quote::parse)
//                .map(Discount::applyDiscount)
//                .collect(Collectors.toList());

        // 使用折扣服务并行方式获取价格
        List<CompletableFuture<String>> priceFutureList = shopList.stream()
                // 使用多线程配置转换为CompletableFuture<String>流
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getDiscountPrice(product)
                        , executor
                ))
                // 使用CompletableFuture.thenApply将CompletableFuture<String>转换为CompletableFuture<Quote>流
                .map(future -> future.thenApply(Quote::parse))
                // 使用CompletableFuture.thenCompose操作流水线将CompletableFuture<Quote>转换为CompletableFuture<String>流
                .map(future -> future.thenCompose(
                        quote -> CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote)
                                , executor
                        )
                ))
                // 合并
                .collect(Collectors.toList());

        // 通过CompletableFuture.join获取返回结果
        return priceFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public Stream<CompletableFuture<String>> findPricesStream(String product){
        return shopList.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getDiscountPrice(product)
                        , executor
                ))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(
                        quote -> CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote)
                                , executor
                        )
                ));
    }

    /**
     * 模拟接口调用的1秒延迟
     */
    public static void delay(){
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void randomDelay(){
        int delay = 500 + random.nextInt(2000);
        try{
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
