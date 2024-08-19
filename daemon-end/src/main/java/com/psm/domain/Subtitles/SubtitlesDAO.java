package com.psm.domain.Subtitles;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_subtitles")
public class SubtitlesDAO implements Serializable {
    private static final long serialVersionUID = 7854067814398096986L;

    @TableId
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String video;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private String category;
    private String createTime;
    private String modifyTime;

    @TableLogic
    private Boolean deleted;

    @Version
    private Integer version;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category{
        private String oriLan;//原始语言
        private String tarLan;//目标语言
    }

    public Category getCategory() {
        return JSON.parseObject(category, Category.class);
    }
}
