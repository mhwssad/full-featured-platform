package com.example.cybz_back.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
public class JSONResult<T> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Integer code;
    private String message;
    private T data;

    /**
     * 成功响应，默认状态码200
     */
    public static <T> JSONResult<T> ok(T data) {
        return ok(HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 成功响应，自定义消息
     */
    public static <T> JSONResult<T> ok(String message, T data) {
        return result(HttpStatus.OK, message, data);
    }

    /**
     * 成功响应，无数据返回
     */
    public static JSONResult<Void> ok() {
        return result(HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), null);
    }

    /**
     * 资源创建成功，状态码201
     */
    public static <T> JSONResult<T> created(T data) {
        return result(HttpStatus.CREATED, HttpStatus.CREATED.getReasonPhrase(), data);
    }

    /**
     * 无内容成功，状态码204
     */
    public static JSONResult<Void> noContent() {
        return result(HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.getReasonPhrase(), null);
    }

    /* 客户端错误响应方法 */

    /**
     * 请求错误，状态码400
     */
    public static JSONResult<Void> badRequest(String message) {
        return result(HttpStatus.BAD_REQUEST, message, null);
    }

    /**
     * 未授权，状态码401
     */
    public static JSONResult<Void> unauthorized(String message) {
        return result(HttpStatus.UNAUTHORIZED, message, null);
    }

    /* 服务端错误响应方法 */

    /**
     * 服务端错误，状态码500
     */
    public static JSONResult<Void> internalError(String message) {
        return result(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }

    /**
     * 禁止访问，状态码403
     */
    public static JSONResult<Void> forbidden(String message) {
        return result(HttpStatus.FORBIDDEN, message, null);
    }

    /**
     * 未找到资源，状态码404
     */
    public static JSONResult<Void> notFound(String message) {
        return result(HttpStatus.NOT_FOUND, message, null);
    }


    /**
     * 根据异常生成内部服务器错误响应
     */
    public static JSONResult<Void> error(Exception e) {
        return internalError(e.getMessage());
    }

    private static <T> JSONResult<T> result(HttpStatus status, String message, T data) {
        return JSONResult.<T>builder()
                .code(status.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> JSONResult<T> custom(int code, String message, T data) {
        return JSONResult.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> JSONResult<T> custom(HttpStatus status, String message, T data) {
        return result(status, message, data);
    }

    /**
     * 将对象转换为JSON字符串
     */
    public String toJsonString() throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(this);
    }

    /**
     * 将JSON字符串转换为对象
     */
    public static <T> JSONResult<T> fromJsonString(String json, Class<T> clazz) throws JsonProcessingException {
        // 使用 JavaType 明确指定泛型类型信息
        JavaType javaType = OBJECT_MAPPER.getTypeFactory()
                .constructParametricType(JSONResult.class, clazz);
        return OBJECT_MAPPER.readValue(json, javaType);
    }
}
