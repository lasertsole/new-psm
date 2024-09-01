package com.psm.domain.User.entity.OAuth2;

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

    private Integer userId;             //第三方登录id

    @TableField("provider")
    private String registrationId;      //第三方登录类型id

    @TableField("provider_user_id")
    private String login;               //第三方登录账号

    private String name;                //第三方登录用户名
    private String avatar;              //第三方用户头像
    private String Credentials;         //第三方用户凭证
    private Date credentialsExpiresAt;  //第三方用户凭证过期时间

    private Date createTime;            //第三方用户创建时间
    private Date updateTime;            //第三方用户更新时间
}
