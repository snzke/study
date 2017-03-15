package org.snzke.lambda;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 拆分器示例
 */
public class SpliteratorDemo {
    public static final String SENTENCE = " Nel   mezzo del cammin   di nostra  vita " +
            "mi  ritrovai in una selva oscura" +
            " ché la  dritta via era  smarrita ";
    public static void main(String [] args){

        System.out.println("一共有 " + countWordsIteratively(SENTENCE) + " 个单词 -> 常规方式");

        Stream<Character> characterStream = IntStream.range(0, SENTENCE.length()).mapToObj(SENTENCE::charAt);
        System.out.println("一共有 " + countWords(characterStream) + " 个单词 -> stream串行");

        Stream<Character> parallelCharacterStream = IntStream.range(0, SENTENCE.length()).mapToObj(SENTENCE::charAt);
        System.out.println("一共有 " + countWords(parallelCharacterStream.parallel()) + " 个单词 -> 无拆分器stream并行（结果错误）");

        Stream<Character> spliteratorParallelCharacterStream = StreamSupport.stream(new WordCounterSpliterator(SENTENCE), true);
        System.out.println("一共有 " + countWords(spliteratorParallelCharacterStream.parallel()) + " 个单词 -> 有拆分器stream并行（结果正确）");
    }

    /**
     * 计算字符串中的单词数量
     * @param s 需要计算单词的字符串
     * @return 单词数量
     */
    public static int countWordsIteratively(String s){
        int counter = 0;
        boolean lastSpace = true;
        char [] charArray = s.toCharArray();
        for (char c : charArray) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            }else{
                if(lastSpace){
                    counter ++;
                }
                lastSpace = false;
            }
        }
        return counter;
    }

    /**
     * 通过流迭代实现单词计数
     * @param stream
     * @return
     */
    private static int countWords(Stream<Character> stream){
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine);
        return wordCounter.getCounter();
    }

    /**
     * 一个可记录单词数的状态类，用于Stream遍历时统计单词数
     */
    static class WordCounter {
        /**
         * 单词数
         */
        private final int counter;
        /**
         * 上一个字符是否为空格
         */
        private final boolean lastSpace;
        public WordCounter(int counter, boolean lastSpace){
            this.counter = counter;
            this.lastSpace = lastSpace;
        }

        /**
         * 遍历计算单词数函数
         * @param c 遍历的当前字符
         * @return
         */
        public WordCounter accumulate(Character c){
            // 以下方式为Java 8 实战中源码

//            if (Character.isWhitespace(c)) {
//                // 无论上一个单词是否为空格，只要当前字符为空格则不加单词数
//                return lastSpace ? this : new WordCounter(counter, true);
//            } else {
//                // 当上一个字符是空格，且当前字符不是空格，则将单词数加1
//                return lastSpace ? new WordCounter(counter + 1, false) : this;
//            }

            // 可优化为以下方式
            boolean space = Character.isWhitespace(c);

            if (lastSpace && !space) {
                // 当上一个字符是空格，且当前字符不是空格，则将单词数加1
                return new WordCounter(counter + 1, false);
            } else {
                // 反之返回当前单词计数器
                return new WordCounter(counter, space);
            }
        }

        /**
         * 整合计算总单词数
         * @param wordCounter
         * @return
         */
        public WordCounter combine(WordCounter wordCounter){
            return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
        }

        /**
         * 获取总单词数
         * @return
         */
        public int getCounter(){
            return counter;
        }
    }
}
