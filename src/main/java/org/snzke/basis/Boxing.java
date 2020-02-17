package org.snzke.basis;

/**
 * Java 自动拆箱封箱实验
 * 猜测以下程序输出结果
 * 程序中应避免使用自动拆箱封箱
 * @author snzke
 */
public class Boxing {
    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d);
        System.out.println(e == f);
        System.out.println(c == (a + b));
        System.out.println(c.equals(a + b));
        System.out.println(g == (a + b));
        System.out.println(g.equals(a + b));
    }
}
