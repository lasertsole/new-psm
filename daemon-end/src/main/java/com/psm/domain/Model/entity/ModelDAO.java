package com.psm.domain.Model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.psm.domain.Model.valueObject.Category;
import com.psm.infrastructure.enums.VisibleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tb_models", autoResultMap = true)
public class ModelDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4967272192276572886L;
    private static final Logger log = LoggerFactory.getLogger(ModelDAO.class);

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
