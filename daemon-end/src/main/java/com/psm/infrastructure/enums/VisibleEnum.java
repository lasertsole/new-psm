package com.psm.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VisibleEnum {
    PUBLIC(0,"公开"),
    PROTECTED(1,"半公开"),
    PRIVATE(2,"未公开");

    @EnumValue
    private final Integer value;
    private final String name;
}
