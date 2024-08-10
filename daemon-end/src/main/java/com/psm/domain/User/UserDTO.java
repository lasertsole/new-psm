package com.psm.domain.User;

import com.psm.annotation.ValidBoolean;
import com.psm.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 2281690874579504458L;

    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "用户名格式错误")
    @Size(min = 3, max = 12, message = "用户名长度必须在3-12之间")
    private String name;

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_*]+$", message = "用户名格式错误")
    @Size(min = 6, max = 16, message = "密码长度必须在6-16之间")
    private String password;

    @Pattern(regexp = "^(?:\\+?(\\d{1,3}))?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?$", message = "手机号码格式错误")
    private String phone;

    @Pattern(regexp = "^(?:\\/[\\w\\-]+)+\\/?$", message = "头像地址格式错误")
    @Size(max = 255, message = "头像地址长度不能超过255")
    private String avatar;

    @Pattern(regexp = "^[\\w\\-]+(\\.[\\w\\-]+)*@[\\w\\-]+(\\.[\\w\\-]+)+$", message = "邮箱地址格式错误")
    @Size(min = 8, max = 255, message = "邮箱地址长度必须在6-32之间")
    private String email;

    @ValidBoolean
    private SexEnum sex;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "简介格式错误")
    @Size(max = 255, message = "简介长度不能超过255")
    private String profile;
}
