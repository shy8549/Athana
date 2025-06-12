package com.tongtech.crypto.sdk.utils;

public class StringOperation {

    public static String AesEncoder(String in) {
        AesEncoder aesEncoder = new AesEncoder();
        return aesEncoder.eval(in,"112323123|||||||");
    }
    public static String AesDecoder(String in) {
        AesDecoder aesDecoder = new AesDecoder();
        return aesDecoder.eval(in,"112323123|||||||");
    }

    // 密钥必须是16字节
    public static String AesEncoderKey(String in, String key) {
        AesEncoder aesEncoder = new AesEncoder();
        return aesEncoder.eval(in,key);
    }
    public static String AesDecoderKey(String in, String key) {
        AesDecoder aesDecoder = new AesDecoder();
        return aesDecoder.eval(in,key);
    }

    public static String maskAll(String in){
        return MaskUtil.maskAll(in);
    }

    public static String maskFront(String in,int maskLength){
        return MaskUtil.maskFront(in,maskLength);
    }

    public static String maskRear(String in, int maskLength){
        return MaskUtil.maskRear(in,maskLength);
    }

    public static String maskMiddle(String in, int startIndex, int endIndex){
        return MaskUtil.maskMiddle(in,startIndex,endIndex);
    }

    public static String maskEnd(String in, int preMaskLength, int suffMaskLength){
        return MaskUtil.maskEnds(in,preMaskLength,suffMaskLength);
    }


    public static void main(String[] args) {

        System.out.println("对 hello 加密，结果： " + AesEncoderKey("hello","1234567890123456"));
        System.out.println("对 hello 加密后解密，结果： " + AesDecoderKey("EBB7C703E675DB3DA397038B4C17823C","1234567890123456"));
        System.out.println("操作字符串 abcdefghijklmnopqrstuvwxyz 进行全部脱敏: " + maskAll("abcdefghijklmnopqrstuvwxyz"));
        System.out.println("操作字符串 abcdefghijklmnopqrstuvwxyz 将前7字符串脱敏: " + maskFront("abcdefghijklmnopqrstuvwxyz",7));
        System.out.println("操作字符串 abcdefghijklmnopqrstuvwxyz 将后9字符串脱敏: " + maskRear("abcdefghijklmnopqrstuvwxyz",9));
        System.out.println("操作字符串 abcdefghijklmnopqrstuvwxyz 将9到16位字符串脱敏: " + maskMiddle("abcdefghijklmnopqrstuvwxyz",9,16));
        System.out.println("操作字符串 abcdefghijklmnopqrstuvwxyz 将前9位与后6位字符串脱敏: " + maskEnd("abcdefghijklmnopqrstuvwxyz",9,6));
    }
}
