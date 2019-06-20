package com.suifeng.javaparsertool.support.utils;

import java.util.Arrays;
import java.util.Random;

/**
 * 随机工具类
 */
public class RandomUtil {
    private static Random _rand;

    static {
        _rand = new Random(System.nanoTime());
    }

    public static boolean randBool() {
        return _rand.nextBoolean();
    }

    public static int randInt(int min, int max) {
        return _rand.nextInt(max - min + 1) + min;
    }

    public static float randFloat(){
        return _rand.nextFloat();
    }

    public static String randWord(int min, int max) {
        Random random = _rand;
        int len = random.nextInt(max - min + 1) + min;

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < len; i++) {
            if (random.nextBoolean())
                buffer.append((char) (random.nextInt('z' - 'a') + 'a'));
            else buffer.append((char) (random.nextInt('Z' - 'A') + 'A'));
        }

        return buffer.toString();
    }

    /**
     * 随机一个config
     * @param group
     * @return
     */
    public static String getRandConfig(String group) {
        String rand = "${@config\\\\rand\\\\rand";
        int num = randInt(1, 5);
        return Utils.isStringEmpty(group) ? rand + num + "}" : rand + num + "|#unique=" + group + "}";
    }

    /**
     * 生成多层级的随机目录
     *
     * @param contact       连接符
     * @param group         分组信息
     * @param min           最少个数
     * @param max           最多个数
     * @param contactBefore config前面是否要带连接符,true-要带
     * @return
     */
    public static String getMultRandConfig(String contact, String group, int min, int max, boolean contactBefore) {
        StringBuffer sb = new StringBuffer(45);
        int count = randInt(min, max);
        if (count == 0) {
            return "";
        }
        if (contactBefore) {
            sb.append(contact);
        }
        for (int i = 1; i <= count; i++) {
            if (i != count) {
                sb.append(getRandConfig(null));
                sb.append(contact);
            } else {
                sb.append(getRandConfig(group));
            }
        }
        return sb.toString();
    }

    public static String randWordNember(int min, int max) {
        Random random = _rand;
        int len = random.nextInt(max - min) + min;

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < len; i++) {
            int v = random.nextInt(3);
            if (v == 0)
                buffer.append((char) (random.nextInt('z' - 'a') + 'a'));
            else if (v == 1)
                buffer.append((char) (random.nextInt('Z' - 'A') + 'A'));
            else buffer.append((char) (random.nextInt('9' - '0') + '0'));
        }

        return buffer.toString();
    }

    public static String randNumber(int min, int max) {
        Random random = _rand;
        int len = random.nextInt(max - min) + min;

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < len; i++) {
            buffer.append((char) (random.nextInt('9' - '0') + '0'));
        }

        return buffer.toString();
    }

    // !小写字母 @大写字母 #数字
    public static String rand(String pattern) {
        Random random = _rand;
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '!')
                buffer.append((char) (random.nextInt('z' - 'a') + 'a'));
            else if (pattern.charAt(i) == '@')
                buffer.append((char) (random.nextInt('Z' - 'A') + 'A'));
            else if (pattern.charAt(i) == '#')
                buffer.append((char) (random.nextInt('9' - '0') + '0'));
            else buffer.append(pattern.charAt(i));
        }

        return buffer.toString();
    }

    public static <T> T rand(T... array) {
        int index = _rand.nextInt(array.length);

        if (array.length == 0)
            return null;
        else return array[index];
    }

    static <T> RandomArray<T> getArray(int def, T[] array) {
        return new RandomArray<T>(def, array);
    }

    static <T> RandomArray<T> getArray(T[] array) {
        return new RandomArray<T>(1, array);
    }

    static class RandomArray<T> {
        T[] _array;
        int[] _powers;
        int[] _updates;
        int _def = 1;
        int _max;

        public RandomArray(int def, T[] array) {
            _array = array;
            _def = def;

            _powers = new int[array.length];
            _updates = new int[array.length + 1];
            Arrays.fill(_powers, def);
        }

        public void setPower(int index, int power) {
            _powers[index] = power;
        }

        public void update() {
            _updates[0] = 0;
            for (int i = 0; i < _powers.length; i++) {
                _updates[i + 1] = _updates[i] + _powers[i];
            }

            _max = _updates[_updates.length - 1];
        }

        public T get() {
            int r = _rand.nextInt(_max);

            return _array[seach(r)];
        }

        int seach(int r) {
            int end = _updates.length - 2;
            int start = 0;
            int index = (end + start) / 2;
            while (true) {
                if (_updates[index] <= r && _updates[index + 1] > r) {
                    return index;
                } else if (_updates[index] > r) {
                    end = index - 1;
                } else if (_updates[index + 1] <= r) {
                    start = index + 1;
                }

                index = (end + start) / 2;
            }
        }
    }
}
