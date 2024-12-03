package com.psm.domain.Model.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.types.convertor.Model3dConvertor;
import com.psm.types.common.DO.DO;
import com.psm.types.enums.VisibleEnum;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.annotation.InsertFillTime;
import com.tangzc.mpe.annotation.InsertUpdateFillTime;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.*;

import java.io.Serializable;

@Data
@AutoDefine
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_3d_models", comment="3D模型表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model3dDO implements Serializable, DO<Model3dBO, Model3dDTO> {
    @ColumnId(comment = "id主键")
    private Long id;

    @Index(name = "tb_3d_models_userId_index")
    @Column(comment = "用户id", notNull = true)
    private Long userId;

    @Column(comment = "模型标题", length = 20)
    private String title;

    @Column(comment = "模型内容", length = 255)
    private String content;

    @Column(comment = "模型封面", length = 255)
    private String cover;

    @Column(comment = "模型实体", length = 255)
    private String entity;

    @Column(comment = "模型可见性")
    private VisibleEnum visible;

    @Column(comment = "模型存储量")
    private Long storage;

    @Column(comment = "模型风格", length = 15)
    private String style;

    @Column(comment = "模型类型", length = 15)
    private String type;

    @Index(name = "tb_3d_models_createTime_index")
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

    @Override
    public Model3dBO toBO() {
        return Model3dConvertor.INSTANCE.DO2BO(this);
    }

    @Override
    public Model3dDTO toDTO() {
        return Model3dConvertor.INSTANCE.DO2DTO(this);
    }
}
