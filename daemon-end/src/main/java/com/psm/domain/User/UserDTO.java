package com.psm.domain.User;

import com.psm.annotation.ValidBoolean;
import com.psm.annotation.ValidFileSize;
import com.psm.annotation.ValidImage;
import com.psm.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -2234378943489471672L;

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
            message = "The phone format is incorrect")
    private String phone;

    @ValidImage
    @ValidFileSize(maxSize = 10 * 1024)//最大10MB
    private MultipartFile avatar;

    @Pattern(regexp = "^[\\w\\-]+(\\.[\\w\\-]+)*@[\\w\\-]+(\\.[\\w\\-]+)+$", message = "The Email format is incorrect")
    @Size(min = 8, max = 255, message = "The Email length must be between 8 and 255 characters")
    private String email;

    @ValidBoolean
    private SexEnum sex;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The profile format is incorrect")
    @Size(max = 255, message = "The profile length must not exceed 255 characters")
    private String profile;
}
