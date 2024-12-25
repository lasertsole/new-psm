package com.psm.types.enums;

import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@JSONType(writeEnumAsJavaBean = true)
public enum VisibleEnum implements Serializable {
    PUBLIC(0,"公开"),
    PROTECTED(1,"半公开"),
    PRIVATE(2,"未公开");

    @EnumValue
    private final Integer value;
    private final String name;

    public static VisibleEnum fromInteger(Integer value) {
        if (value == null) {
            return null;
        }
        for (VisibleEnum visibleEnum : values()) {
            if (visibleEnum.getValue().equals(value)) {
                return visibleEnum;
            }
        }
        throw new IllegalArgumentException("未知的值: " + value);
    }
}
