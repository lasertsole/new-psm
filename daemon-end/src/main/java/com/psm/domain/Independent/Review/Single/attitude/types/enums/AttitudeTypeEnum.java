package com.psm.domain.Independent.Review.Single.attitude.types.enums;

import com.alibaba.fastjson2.annotation.JSONCreator;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AttitudeTypeEnum {
    Like("Like"),
    Dislike("Dislike");

    @EnumValue
    @JSONField(name = "attitudeType")
    private final String name;

    @JSONCreator
    public static AttitudeTypeEnum fromName(String name) {
        return Arrays.stream(values())
                .filter(type -> type.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TargetTypeEnum name: " + name));
    }
}
