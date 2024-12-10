package com.datacolumnoperate.exceptions;

/**
 * BaseBusinessException 是所有业务逻辑相关异常的基类。
 * 可用于统一处理和捕获业务逻辑中的异常。
 */
public class BaseBusinessException extends RuntimeException {
    /**
     * 构造函数，带错误消息。
     *
     * @param message 错误消息
     */
    public BaseBusinessException(String message) {
        super(message);
    }

    /**
     * 构造函数，带错误消息和引发的根异常。
     *
     * @param message 错误消息
     * @param cause   引发的根异常
     */
    public BaseBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

