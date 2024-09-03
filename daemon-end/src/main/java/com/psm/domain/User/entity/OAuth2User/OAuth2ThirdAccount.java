package com.psm.domain.User.entity.OAuth2User;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_third_party_user")
public class OAuth2ThirdAccount implements Serializable {
    private static final long serialVersionUID = 565652969569896022L;

    @TableId
    private Integer id;                 //自增id

    private Integer userId;             //外键用户id

    @TableField("provider")
    private String registrationId;      //第三方登录类型id

    private String providerUserId;               //第三方登录账号id

    private String name;                //第三方登录用户名
    private String avatar;              //第三方用户头像

    @TableField("access_token")
    private String Credentials;         //第三方用户凭证

    @TableField("token_expires_at")
    private Date credentialsExpiresAt;  //第三方用户凭证过期时间
}
