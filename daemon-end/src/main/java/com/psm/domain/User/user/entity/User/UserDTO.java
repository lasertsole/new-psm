package com.psm.domain.User.user.entity.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.app.annotation.validation.ValidFileSize;
import com.psm.app.annotation.validation.ValidImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The username format is incorrect")
    @Size(min = 3, max = 12, message = "The username length must be between 3 and 12 characters")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9_*]+$", message = "The password format is incorrect")
    @Size(min = 8, max = 26, message = "The password length must be between 8 and 26 characters")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9_*]+$", message = "The changePassword length must be between 6 and 16 characters")
    @Size(min = 8, max = 26, message = "The changePassword length must be between 8 and 26 characters")
    private String changePassword;


    @Pattern(regexp = "^(?:\\+?(\\d{1,3}))?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()" +
            "\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?[-. ()\\d]*(\\d{1,4})?$",
            message = "The phone format is incorrect")//普通号码11位，国际区号3位，可选+号1位
    private String phone;

    @ValidImage
    @ValidFileSize(maxSize = 10 * 1024)//最大10MB
    private MultipartFile avatar;

    @Pattern(regexp = "^[\\w\\-]+(\\.[\\w\\-]+)*@[\\w\\-]+(\\.[\\w\\-]+)+$", message = "The Email format is incorrect")
    @Size(max = 255, message = "The avatarUrl length must not exceed 255 characters")
    private String oldAvatarUrl;

    @Pattern(regexp = "^[\\w\\-]+(\\.[\\w\\-]+)*@[\\w\\-]+(\\.[\\w\\-]+)+$", message = "The Email format is incorrect")
    @Size(min = 8, max = 255, message = "The Email length must be between 8 and 255 characters")
    private String email;

    private Boolean sex;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The profile format is incorrect")
    @Size(max = 255, message = "The profile length must not exceed 255 characters")
    private String profile;

    @Min(value = 0, message = "The publicModelNum must be greater than or equal to 0")
    private Short publicModelNum;

    @Min(value = 0, message = "The modelMaxStorage must be greater than or equal to 0")
    private Long modelMaxStorage;

    @Min(value = 0, message = "The modelCurStorage must be greater than or equal to 0")
    private Long modelCurStorage;

    private Boolean isIdle;

    private Boolean canUrgent;
}