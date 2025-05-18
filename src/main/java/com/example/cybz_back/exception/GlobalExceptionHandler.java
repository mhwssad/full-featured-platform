package com.example.cybz_back.exception;

import com.example.cybz_back.utils.JSONResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public JSONResult<List<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return JSONResult.custom(400, "参数校验失败", errors);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public JSONResult<List<String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        log.error("Spring参数校验失败: {}", errors);
        return JSONResult.custom(400, "参数校验失败", errors);
    }

    @ExceptionHandler(EmailProcessingException.class)
    @ResponseBody
    public JSONResult<String> handleEmailProcessingException(EmailProcessingException ex) {
        log.error("邮件处理异常: {}", ex.getMessage());
        return JSONResult.custom(500, "邮件处理异常", ex.getMessage());
    }

    @ExceptionHandler(FrequentRequestException.class)
    @ResponseBody
    public ResponseEntity<JSONResult<Void>> handleFrequentRequestException(FrequentRequestException e) {
        log.error("频繁请求: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(JSONResult.error(e));
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseBody
    public JSONResult<Void> handleJsonProcessingException(JsonProcessingException e) {
        log.error("JSON序列化失败: {}", e.getMessage());
        return JSONResult.internalError("系统数据格式异常");
    }
}
