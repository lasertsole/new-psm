package com.psm.domain.Subtitles.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.annotation.InsertFillTime;
import com.tangzc.mpe.annotation.InsertUpdateFillTime;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AutoDefine
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_subtitles", comment="视频表")
public class SubtitlesDO implements Serializable {
    @ColumnId(comment = "id主键")
    private Long id;

    @Index(name = "tb_subtitles_userId_index")
    @Column(comment = "用户id", notNull = true)
    private Long userId;

    @Column(comment = "视频标题", length = 20)
    private String title;

    @Column(comment = "视频内容", length = 255)
    private String content;

    @Column(comment = "视频封面", length = 255)
    private String cover;

    @Column(comment = "视频实体", length = 255)
    private String video;

    @Column(comment = "视频风格", length = 15)
    private String style;

    @Column(comment = "视频类型", length = 15)
    private String type;

    @Index(name = "tb_subtitles_createTime_index")
    @InsertFillTime
    @Column(comment = "创建时间")
    private String createTime;

    @InsertUpdateFillTime
    @Column(comment = "最后更新时间")
    private String modifyTime;

    @TableLogic
    @Column(comment = "逻辑删除", defaultValue = "false", notNull = true)
    private Boolean deleted;

    @Version
    @Column(comment = "乐观锁版本控制", defaultValue = "0")
    private Integer version;
}
