package com.psm.domain.Model.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.valueObject.Category;
import com.psm.infrastructure.enums.VisibleEnum;
import com.psm.infrastructure.utils.MybatisPlus.JsonTypeHandler;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(value = "tb_models", autoResultMap = true)
public class ModelDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2594689620389781412L;

    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private VisibleEnum visible;
    private Long storage;

    @TableField(typeHandler = JsonTypeHandler.class)
    private Category category;
    private String createTime;
    private String modifyTime;

    @TableLogic
    private Boolean deleted;

    @Version
    private Integer version;
}
