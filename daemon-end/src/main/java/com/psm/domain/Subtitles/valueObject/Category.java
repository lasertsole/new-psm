package com.psm.domain.Subtitles.valueObject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Category {
    private String oriLan = "";//原始语言
    private String tarLan = "";//目标语言

    public void setOriLan(String oriLan) {
        if (!StringUtils.isBlank(this.oriLan)) return;
        this.oriLan = oriLan;
    }

    public void setTarLan(String tarLan) {
        if (!StringUtils.isBlank(this.tarLan)) return;
        this.tarLan = tarLan;
    }
}