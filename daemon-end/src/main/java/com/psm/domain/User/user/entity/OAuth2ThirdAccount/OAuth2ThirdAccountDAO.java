package com.psm.domain.User.user.entity.OAuth2ThirdAccount;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_third_party_users")
public class OAuth2ThirdAccountDAO implements Serializable {
    private static final long serialVersionUID = 565652969569896022L;

    @TableId
    private Long id;                 //自增id

    private Long userId;             //外键用户id

    @TableField("provider")
    private String registrationId;      //第三方登录类型id

    private String providerUserId;               //第三方登录账号id

    @TableField("access_token")
    private String Credentials;         //第三方用户凭证

    @TableField("token_expires_at")
    private Date credentialsExpiresAt;  //第三方用户凭证过期时间
}
