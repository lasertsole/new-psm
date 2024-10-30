package com.psm.domain.Model.model.valueObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {
    private String style = "";//模型风格
    private String type = "";//模型类型
}