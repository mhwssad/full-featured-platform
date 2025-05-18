package com.example.cybz_back.exception;

public class FrequentRequestException extends RuntimeException {
    /**
     * 构造函数，接收异常消息作为参数
     *
     * @param message 异常消息，描述频繁请求的相关信息
     */
    public FrequentRequestException(String message) {
        super(message);
    }

    /**
     * 构造函数，接收异常消息和异常原因作为参数
     *
     * @param message 异常消息，描述频繁请求的相关信息
     * @param cause   异常原因，即导致频繁请求异常的底层原因
     */
    public FrequentRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
