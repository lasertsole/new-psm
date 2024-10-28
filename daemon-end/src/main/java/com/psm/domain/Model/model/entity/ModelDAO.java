package com.psm.domain.Model.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.psm.domain.Model.model.valueObject.Category;
import com.psm.infrastructure.enums.VisibleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tb_models", autoResultMap = true)
public class ModelDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3090621278404553476L;

    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private VisibleEnum visible;
    private Long storage;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Category category;
    private String createTime;
    private String modifyTime;

    @TableLogic
    private Boolean deleted;

    @Version
    private Integer version;
}
