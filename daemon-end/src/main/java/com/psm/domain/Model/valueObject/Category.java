package com.psm.domain.Model.valueObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public class Category {
    private String style = "";//模型风格
    private String type = "";//模型类型

    public void setStyle(String style) {
        if (!StringUtils.isBlank(this.style)) return;
        this.style = style;
    }

    public void setType(String type) {
        if (!StringUtils.isBlank(this.type)) return;
        this.type = type;
    }
}