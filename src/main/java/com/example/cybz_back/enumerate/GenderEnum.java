package com.example.cybz_back.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum GenderEnum {
    MAN("man", "男"),
    WOMAN("woman", "女"),
    UNKNOWN("unknown", "未知");

    @EnumValue
    private final String code;
    private final String message;

    GenderEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // 使用 Map 提高查询效率
    private static final Map<String, GenderEnum> CODE_MAP = new HashMap<>();

    static {
        for (GenderEnum gender : values()) {
            CODE_MAP.put(gender.getCode(), gender);
        }
    }

    public static GenderEnum fromCode(String code) {
        return CODE_MAP.getOrDefault(code, UNKNOWN);
    }

}
