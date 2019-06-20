package com.suifeng.javaparsertool.support.utils;


/**
 * 工具类
 */
public class Utils {

    /**
     * 把字符串的指定字符转为大写
     * 暂不考虑非小写字母情况
     * @param str
     * @param index
     * @return
     */
    public static String upperCaseChar(String str,int index) {
        char[] cs=str.toCharArray();
        cs[index]-=32;
        return String.valueOf(cs);
    }

    /**
     * 是否空字符串
     * @param str
     * @return true 为空
     */
    public static boolean isStringEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
