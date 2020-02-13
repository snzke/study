package org.snzke.lambda;

import java.util.Arrays;
import java.util.List;

/**
 * 为学习Java 8 特性而创建的菜品类
 */
public class Dish {
    /** 名称 */
    private final String name;
    /** 素菜标识 */
    private final boolean vegetarian;
    /** 卡路里(热量) */
    private final int calories;
    /** 类型 */
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    public CaloricLevel getCaloricLevel(){
        if(getCalories() <= 400) { return CaloricLevel.健康; }
        else if(getCalories() <= 700) { return CaloricLevel.普通; }
        else { return CaloricLevel.高脂肪; }
    }

    /**
     * 菜品类型
     */
    public enum Type {
        肉, 鱼, 其他
    }

    /**
     * 热量级别
     */
    public enum CaloricLevel{
        健康, 普通, 高脂肪
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * 默认菜单
     */
    public static final List<Dish> MENU =
            Arrays.asList( new Dish("回锅肉", false, 800, Dish.Type.肉),
                    new Dish("水煮牛肉", false, 700, Dish.Type.肉),
                    new Dish("宫保鸡丁", false, 400, Dish.Type.肉),
                    new Dish("青椒土豆丝", true, 530, Dish.Type.其他),
                    new Dish("米饭", true, 350, Dish.Type.其他),
                    new Dish("水果拼盘", true, 120, Dish.Type.其他),
                    new Dish("南瓜烩饼", true, 550, Dish.Type.其他),
                    new Dish("酸菜鱼", false, 400, Dish.Type.鱼),
                    new Dish("豆瓣鱼", false, 450, Dish.Type.鱼));
}