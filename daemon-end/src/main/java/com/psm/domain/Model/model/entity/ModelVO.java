package com.psm.domain.Model.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.valueObject.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7517370696763321983L;

    private String id;
    private String userId;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private String style;//模型风格
    private String type;//模型类型
    private Integer visible;
    private Long storage;
    private String createTime;
}
