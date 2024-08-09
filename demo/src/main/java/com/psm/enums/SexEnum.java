package com.psm.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SexEnum {
    MALE(false,"男"),
    FEMAIL(true,"女");

    @EnumValue
    private final Boolean sex;
    private final String sexName;

    SexEnum(Boolean sex, String sexName) {
        this.sex = sex;
        this.sexName = sexName;
    }
}
