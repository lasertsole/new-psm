package com.psm.domain.Showcase;

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
@TableName("tb_showcase")
public class ShowcaseDAO implements Serializable {
    private static final long serialVersionUID = -7624453654256212950L;

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
        private String oriLang;
        private String tarLang;
    }

    public Category getCategory() {
        return JSON.parseObject(category, Category.class);
    }
}
