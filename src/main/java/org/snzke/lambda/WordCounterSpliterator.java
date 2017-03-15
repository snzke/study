package org.snzke.lambda;

import java.util.*;
import java.util.function.Consumer;

/**
 * 单词计数拆分器
 * 用于并行迭代时将字符串有序有效拆分
 */
public class WordCounterSpliterator implements Spliterator<Character> {
    /**
     * 拆分的最小维度字符串
     */
    private final String str;
    /**
     * 拆分开始位置
     */
    private int startPos = 0;

    public WordCounterSpliterator(String str){
        this.str = str;
    }

    /**
     * 内部迭代代码将调用此函数尝试迭代
     * @param action 消费者事件，由调用者传入
     * @return 是否继续迭代
     */
    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(str.charAt(startPos++));
        return startPos < str.length();
    }

    /**
     * 尝试拆分流
     * @return 新的拆分器
     */
    @Override
    public Spliterator<Character> trySplit() {
        // 获取当前拆分的长度
        int currentSize = str.length() - startPos;
        // 如果拆分长度小于10，则结束拆分
        if (currentSize < 10) {
            return null;
        }
        // 使用二分法将字符串进行拆分
        for (int splitPos = currentSize / 2 + startPos; splitPos < str.length(); splitPos++){
            // 判断要拆分的字符是否为空格，如果不为空格则跳过，避免破坏字符串结构
            if(Character.isWhitespace(str.charAt(splitPos))){
                // 将开始位置到拆分位置的字符串截取下来继续进行拆分
                Spliterator<Character> spliterator = new WordCounterSpliterator(str.substring(startPos, splitPos));

                // 记录新的开始位置
                startPos = splitPos;

                return spliterator;
            }
        }

        // 结束拆分
        return null;
    }

    /**
     * 获取待遍历元素长度
     * @return 待遍历元素长度
     */
    @Override
    public long estimateSize() {
        return str.length() - startPos;
    }

    /**
     * 获取该拆分器/容器/元素特点<br/>
     * {@link #ORDERED}     容器是有序的（{@link List}）<br/>
     * {@link #DISTINCT}    元素是唯一的（{@link Set}）<br/>
     * {@link #SORTED}      容器是已排序的（{@link NavigableSet},{@link SortedSet}）<br/>
     * {@link #SIZED}       容器是已知大小的（{@link HashSet}）<br/>
     * {@link #NONNULL}     元素是非空的（{@link Hashtable}）<br/>
     * {@link #IMMUTABLE}   容器/元素不可修改的<br/>
     * {@link #CONCURRENT}  元素是线程安全的<br/>
     * {@link #SUBSIZED}    拆分后的子容器是已知大小的<br/>
     * @return 拆分器特点
     */
    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
