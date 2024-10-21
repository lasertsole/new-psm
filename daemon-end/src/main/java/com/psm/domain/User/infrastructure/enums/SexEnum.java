package com.psm.domain.User.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SexEnum {
    MALE(false,"男"),
    FEMALE(true,"女");

    @EnumValue
    private final Boolean value;
    private final String name;
}
