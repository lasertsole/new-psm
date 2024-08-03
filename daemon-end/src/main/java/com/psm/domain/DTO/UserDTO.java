package com.psm.domain.DTO;

import com.psm.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -1424057564144045225L;

    private String name;
    private String password;
    private String phone;
    @Email
    private String email;
    private SexEnum sex;
}
