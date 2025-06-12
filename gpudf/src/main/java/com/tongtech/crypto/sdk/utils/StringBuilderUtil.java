package com.tongtech.crypto.sdk.utils;

/**
 * @Description:
 * @Author: liangpeng
 * @Date: 2024/7/18 14:06:43
 **/
public class StringBuilderUtil {

    private static ThreadLocal<StringBuilder> localStringBuilder = new ThreadLocal<StringBuilder>() {
        protected synchronized StringBuilder initialValue() {
            return new StringBuilder(16);
        }
    };

    public static StringBuilder getLocalStringBuilder(String str) {
        StringBuilder sb = localStringBuilder.get();
        sb.setLength(0);
        sb.append(str);
        return sb;
    }

    public static StringBuilder getLocalStringBuilder() {
        StringBuilder sb = localStringBuilder.get();
        sb.setLength(0);
        return sb;
    }
}
