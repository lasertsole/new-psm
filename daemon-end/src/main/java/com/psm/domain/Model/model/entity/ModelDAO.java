package com.psm.domain.Model.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.types.enums.VisibleEnum;
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
    private static final long serialVersionUID = 9181763329672463766L;

    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private VisibleEnum visible;
    private Long storage;

    private String style;//模型风格
    private String type;//模型类型
    private String createTime;
    private String modifyTime;

    @TableLogic
    private Boolean deleted;

    @Version
    private Integer version;
}
