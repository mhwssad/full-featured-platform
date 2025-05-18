package com.example.cybz_back.validation.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD) // 只能加在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
}
