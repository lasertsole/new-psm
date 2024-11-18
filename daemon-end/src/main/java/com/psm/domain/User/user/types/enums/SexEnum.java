package com.psm.domain.User.user.types.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SexEnum {
    MALE(false,"男"),
    FEMALE(true,"女");

    public static SexEnum fromBoolean(Boolean value) {
        if (Objects.isNull(value)) return null;
        return value ? SexEnum.FEMALE : SexEnum.MALE;
    }

    @EnumValue
    private final Boolean value;
    private final String name;
}
