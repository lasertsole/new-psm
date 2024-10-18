package com.psm.domain.Model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.psm.domain.Model.valueObject.Category;
import com.psm.infrastructure.enums.VisibleEnum;
import com.psm.infrastructure.utils.MybatisPlus.JsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_models")
public class ModelDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4754691025351912230L;

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
