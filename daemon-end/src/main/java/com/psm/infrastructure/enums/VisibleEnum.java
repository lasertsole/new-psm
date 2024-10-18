package com.psm.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VisibleEnum {
    PRIVATE(0,"未公开"),
    PROTECTED(1,"半公开"),
    PUBLIC(2,"公开");

    @EnumValue
    private final Integer visible;
    private final String visibleName;
}
