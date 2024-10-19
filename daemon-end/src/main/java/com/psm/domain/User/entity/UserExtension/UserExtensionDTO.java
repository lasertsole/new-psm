package com.psm.domain.User.entity.UserExtension;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExtensionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -33874665563728703L;

    private Long id;
    private Short modelNum;
    private Long modelCurStorage;
    private Long modelMaxStorage;
    private String createTime;
    private String modifyTime;

    public UserExtensionDTO(Long id) {
        this.id = id;
    }
}
