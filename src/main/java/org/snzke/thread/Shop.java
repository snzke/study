package org.snzke.thread;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.snzke.thread.BestPriceFinder.delay;
import static org.snzke.thread.BestPriceFinder.randomDelay;

/**
 * 店铺实体
 */
public class Shop {
    private static Random random = new Random();
    private String name;
    public Shop(){

    }
    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取带有折扣的价格
     * @param product
     * @return
     */
    public String getDiscountPrice(String product){
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }

    /**
     * 常规方式获取价格
     * @param product
     * @return
     */
    public double getPrice(String product){
        return calculatePrice(product);
    }

    /**
     * 多线程方式获取价格
     * @param product
     * @return
     */
    public Future<Double> getPriceAsync(String product){
        // 常规方式创建Future对象
//        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
//        new Thread(() -> {
//            try{
//                double price = calculatePrice(product);
//                futurePrice.complete(price);
//            }catch (Exception e){
//                // 为避免线程中出现异常导致线程中断，必须捕获异常，并通知Future
//                futurePrice.completeExceptionally(e);
//            }
//        }).start();
//        return futurePrice;

        // 工厂方法创建Future对象
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    /**
     * 模拟调用RPC接口获取价格
     * @param product
     * @return
     */
    private double calculatePrice(String product){
        randomDelay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
}
