package org.snzke.thread;

import java.util.concurrent.*;

/**
 * Futrue 示例
 */
public class FutureDemo {
    private static BestPriceFinder bestPriceFinder = new BestPriceFinder();

    public static void main(String [] args){
//        simpleDemo();
//        testNormalFuture();
//        testMultiThreadFuture();
        testMultiThreadFutureNotOnSameTimeReturn();
    }

    /**
     * Future类简单示例
     */
    public static void simpleDemo(){
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Double> future = executor.submit(() -> {
            System.out.println("正在计算当中...");
            System.out.println(1/0);// 模拟线程执行时异常
            Thread.sleep(2000);     // 模拟获取线程结果超时
            return 8.1;
        });

        try{
            // 阻塞获取线程执行结果，设置超时时间为1秒
            Double result = future.get(1, TimeUnit.SECONDS);
            System.out.println("线程执行结果：" + result);
        } catch (InterruptedException e) {  // 当前线程在过程中被中断
            e.printStackTrace();
        } catch (ExecutionException e) {    // 执行时异常
            e.printStackTrace();
        } catch (TimeoutException e) {      // 在Future对象完成之前超时
            System.err.println("等待线程超时...");
            e.printStackTrace();
        }
    }

    /**
     * 测试常规方式获取价格
     */
    public static void testNormalFuture(){
        long start = System.nanoTime();
        Future<Double> futurePrice = new Shop().getPriceAsync("小米 MIX");
        long invocationTime = ((System.nanoTime() - start) / 100_0000);

        System.out.println("开始获取最佳价格，耗时：" + invocationTime + "毫秒");

        // 获取其他店铺价格

        try {
            double price = futurePrice.get();
            System.out.printf("价格为：%.2f%n", price);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        long retrievalTime = ((System.nanoTime() - start) / 100_0000);
        System.out.println("价格查询完毕，耗时：" + retrievalTime + "毫秒");
    }

    /**
     * 测试多线程方式获取价格
     */
    public static void testMultiThreadFuture(){
        long start = System.nanoTime();
        System.out.println(bestPriceFinder.findPrices("华为 P10"));
        long invocationTime = ((System.nanoTime() - start) / 100_0000);
        System.out.println("查询完毕，耗时：" + invocationTime + "毫秒");
    }

    /**
     * 测试多线程方式获取价格
     */
    public static void testMultiThreadFutureNotOnSameTimeReturn(){
        long start = System.nanoTime();
        CompletableFuture<String>[] futures = (CompletableFuture<String>[]) bestPriceFinder.findPricesStream("华为 P10")
            .map(f -> f.thenAccept(
                    s -> System.out.println(s + " 查询完毕，耗时：" + ((System.nanoTime() - start) / 100_0000)))
            ).toArray(size -> new CompletableFuture[size]);
        CompletableFuture.allOf(futures).join();
        long invocationTime = ((System.nanoTime() - start) / 100_0000);
        System.out.println("全部查询完毕，耗时：" + invocationTime + "毫秒");
    }
}
