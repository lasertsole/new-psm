package com.psm.domain.Subtitles.infrastructure.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LanEnum {
    Chinese(0,"zh"),
    English(1,"en"),
    Japanese(2,"jp");

    @EnumValue
    private final Integer lan;
    private final String lanName;
}
