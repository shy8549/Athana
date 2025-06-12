package com.tongtech.crypto.sdk.utils;

/**
 * 字符串脱敏工具类
 */
public final class MaskUtil {

    private MaskUtil() {
        // 私有构造器防止实例化
    }

    /**
     * 将整个字符串替换为*
     * @param targetStr 目标字符串
     * @return 脱敏后的字符串
     */
    public static String maskAll(String targetStr) {
        if (isEmpty(targetStr)) {
            return targetStr;
        }
        return repeat('*', targetStr.length());
    }

    /**
     * 将字符串前maskLength位替换为*
     * @param targetStr 目标字符串
     * @param maskLength 需要脱敏的长度
     * @return 脱敏后的字符串
     * @throws IllegalArgumentException 如果maskLength小于0
     */
    public static String maskFront(String targetStr, int maskLength) {
        if (maskLength < 0) {
            throw new IllegalArgumentException("maskLength cannot be negative");
        }

        if (isEmpty(targetStr) || targetStr.length() <= maskLength) {
            return targetStr;
        }

        return repeat('*', maskLength) + targetStr.substring(maskLength);
    }

    /**
     * 将字符串后maskLength位替换为*
     * @param targetStr 目标字符串
     * @param maskLength 需要脱敏的长度
     * @return 脱敏后的字符串
     * @throws IllegalArgumentException 如果maskLength小于0
     */
    public static String maskRear(String targetStr, int maskLength) {
        if (maskLength < 0) {
            throw new IllegalArgumentException("maskLength cannot be negative");
        }

        if (isEmpty(targetStr) || targetStr.length() <= maskLength) {
            return targetStr;
        }

        int keepLength = targetStr.length() - maskLength;
        return targetStr.substring(0, keepLength) + repeat('*', maskLength);
    }

    /**
     * 将字符串中间从startIndex到endIndex的部分替换为*
     * @param targetStr 目标字符串
     * @param startIndex 开始脱敏的位置(包含)
     * @param endIndex 结束脱敏的位置(包含)
     * @return 脱敏后的字符串
     * @throws IllegalArgumentException 如果startIndex或endIndex小于0，或endIndex小于等于startIndex
     */
    public static String maskMiddle(String targetStr, int startIndex, int endIndex) {
        if (startIndex < 0 || endIndex < 0) {
            throw new IllegalArgumentException("startIndex and endIndex cannot be negative");
        }
        if (endIndex <= startIndex) {
            throw new IllegalArgumentException("endIndex must be greater than startIndex");
        }

        if (isEmpty(targetStr) || targetStr.length() <= endIndex) {
            return targetStr;
        }

        return targetStr.substring(0, startIndex)
                + repeat('*', endIndex - startIndex + 1)
                + targetStr.substring(endIndex + 1);
    }

    /**
     * 将字符串前preMaskLength位和后suffMaskLength位替换为*
     * @param targetStr 目标字符串
     * @param preMaskLength 前部脱敏长度
     * @param suffMaskLength 后部脱敏长度
     * @return 脱敏后的字符串
     * @throws IllegalArgumentException 如果preMaskLength或suffMaskLength小于0
     */
    public static String maskEnds(String targetStr, int preMaskLength, int suffMaskLength) {
        if (preMaskLength < 0 || suffMaskLength < 0) {
            throw new IllegalArgumentException("preMaskLength and suffMaskLength cannot be negative");
        }

        if (isEmpty(targetStr) || targetStr.length() <= preMaskLength) {
            return targetStr;
        }

        if (targetStr.length() <= preMaskLength + suffMaskLength) {
            return repeat('*', preMaskLength) + targetStr.substring(preMaskLength);
        }

        int keepLength = targetStr.length() - preMaskLength - suffMaskLength;
        return repeat('*', preMaskLength)
                + targetStr.substring(preMaskLength, preMaskLength + keepLength)
                + repeat('*', suffMaskLength);
    }

    // 辅助方法：判断字符串是否为空
    private static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    // 高效生成重复字符的字符串
    private static String repeat(char c, int count) {
        if (count <= 0) {
            return "";
        }
        char[] result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }

    public static void main(String[] args) {
        System.out.println(maskAll("abcdefghijk"));
        System.out.println(maskFront("abcdefghijk",5));
        System.out.println(maskRear("abcdefghijk",6));
        System.out.println(maskMiddle("abcdefghijk",3,6));
        System.out.println(maskEnds("abcdefghijk",3,5));
    }
}
