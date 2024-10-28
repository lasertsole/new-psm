package com.psm.domain.Model.model.valueObject;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String style = "";//模型风格
    private String type = "";//模型类型
}