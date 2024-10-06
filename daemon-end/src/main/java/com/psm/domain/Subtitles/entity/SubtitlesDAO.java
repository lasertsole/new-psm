package com.psm.domain.Subtitles.entity;

import com.psm.domain.Subtitles.valueObject.Category;
import com.baomidou.mybatisplus.annotation.*;
import com.psm.infrastructure.utils.MybatisPlus.JsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_subtitles")
public class SubtitlesDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7854067814398096986L;

    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String video;

    @TableField(typeHandler = JsonTypeHandler.class)
    private Category category;
    private String createTime;
    private String modifyTime;

    @TableLogic
    private Boolean deleted;

    @Version
    private Integer version;
}
