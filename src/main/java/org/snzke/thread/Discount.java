package org.snzke.thread;

import static org.snzke.thread.BestPriceFinder.delay;

/**
 * 店铺折扣枚举
 */
public class Discount {
    public enum Code{
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
        private final int percentage;
        Code(int percentage){
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote){
        return quote.getShopName() + " 的价格是 " + Discount.apply(quote.getPrice(), quote.getDiscountCode());
    }

    private static double apply(double price, Code discountCode) {
        // 模拟申请折扣所需耗时
        delay();
        return price * (100 - discountCode.percentage) / 100;
    }
}
