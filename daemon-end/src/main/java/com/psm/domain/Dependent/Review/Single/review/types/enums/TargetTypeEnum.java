package com.psm.domain.Dependent.Review.Single.review.types.enums;

import com.alibaba.fastjson2.annotation.JSONCreator;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TargetTypeEnum {
    Model3d("Model3d"),
    Model2d("Model2d");

    @EnumValue
    @JSONField(name = "targetType")
    private final String name;

    @JSONCreator
    public static TargetTypeEnum fromName(String name) {
        return Arrays.stream(values())
                .filter(type -> type.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TargetTypeEnum name: " + name));
    }
}
