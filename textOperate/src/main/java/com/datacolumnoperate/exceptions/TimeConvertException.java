package com.datacolumnoperate.exceptions;

/**
 * TimeConvertException 表示在时间格式转换过程中发生的异常。
 */
public class TimeConvertException extends BaseBusinessException {
    /**
     * 构造函数，带错误消息。
     *
     * @param message 错误消息
     */
    public TimeConvertException(String message) {
        super(message);
    }

    /**
     * 构造函数，带错误消息和引发的根异常。
     *
     * @param message 错误消息
     * @param cause   引发的根异常
     */
    public TimeConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}

