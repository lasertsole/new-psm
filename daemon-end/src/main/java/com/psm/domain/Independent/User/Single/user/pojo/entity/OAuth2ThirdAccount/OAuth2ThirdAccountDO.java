package com.psm.domain.Independent.User.Single.user.pojo.entity.OAuth2ThirdAccount;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnType;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AutoDefine
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_third_party_users", comment="第三方用户表")
public class OAuth2ThirdAccountDO implements Serializable {
    @Index(name = "tb_third_party_id_index")
    @ColumnId(comment = "id主键")
    private Long id;                 //自增id

    @Index(name = "tb_third_party_user_id_index")
    @Column(comment = "用户id", notNull = true)
    private Long userId;             //外键用户id

    @ColumnComment("第三方登录类型id")
    @ColumnType(length = 255)
    @TableField("provider")
    private String registrationId;      //第三方登录类型id

    @Index(name = "tb_third_party_provider_user_id_index")
    @Column(comment = "第三方用户id", notNull = true, length = 255)
    private String providerUserId;               //第三方登录账号id

    @Index(name = "tb_third_party_provider_user_name_index")
    @ColumnComment("第三方用户昵称")
    @ColumnType(length = 255)
    @TableField("access_token")
    private String Credentials;         //第三方用户凭证

    @ColumnComment("第三方用户头像")
    @ColumnType(length = 255)
    @TableField("token_expires_at")
    private Date credentialsExpiresAt;  //第三方用户凭证过期时间
}
