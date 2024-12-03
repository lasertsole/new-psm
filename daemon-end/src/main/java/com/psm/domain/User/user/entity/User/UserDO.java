package com.psm.domain.User.user.entity.User;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import com.psm.domain.User.user.types.enums.SexEnum;
import com.psm.types.common.DO.DO;
import com.tangzc.autotable.annotation.*;
import com.tangzc.mpe.annotation.InsertFillTime;
import com.tangzc.mpe.annotation.InsertUpdateFillTime;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AutoDefine
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_users", comment="用户表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDO implements Serializable, DO<UserBO, UserDTO> {
    @ColumnId(comment = "id主键")
    private Long id;

    @Column(comment = "用户名", notNull = true, length = 12)
    private String name;

    @Column(comment = "密码", notNull = true, length = 60)// BCrypt加密密码长度60位
    private String password;

    @Index(name = "tb_users_phone_index")
    @Column(comment = "手机号(普通号码11位，国际区号3位，可选+号1位)", length = 15)
    private String phone;

    @Column(comment = "头像", length = 255)
    private String avatar;

    @UniqueIndex(name = "tb_users_email_unique_index")
    @Column(comment = "邮箱", length = 255)
    private String email;

    @Column(comment = "性别")
    private SexEnum sex;

    @Column(comment = "简介", length = 255)
    private String profile;

    @Column(comment = "公开模型数量", defaultValue = "0", notNull = true)
    private Short publicModelNum;

    @Column(comment = "模型最大存储量，默认5G", defaultValue = "5368709120", notNull = true)
    private Long modelMaxStorage;

    @Column(comment = "模型当前存储量", defaultValue = "0", notNull = true)
    private Long modelCurStorage;

    @Column(comment = "是否空闲", defaultValue = "true", notNull = true)
    private Boolean isIdle;

    @Column(comment = "是否紧急", defaultValue = "true", notNull = true)
    private Boolean canUrgent;

    @Index(name = "tb_users_createTime_index")
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

    public static UserDO fromBO(UserBO userBO) {
        return UserConvertor.INSTANCE.BO2DO(userBO);
    }

    @Override
    public UserBO toBO() {
        return UserConvertor.INSTANCE.DO2BO(this);
    }

    @Override
    public UserDTO toDTO() {
        return UserConvertor.INSTANCE.DO2OtherDTO(this);
    }
}
