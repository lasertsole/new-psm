package com.psm.domain.User.entity.UserExtension;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExtensionBO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8062732191014847229L;

    private Long id;
    private short work_num = 0;
    private String createTime;
    private String modifyTime;

    public UserExtensionBO(Long id) {
        this.id = id;
    }
}
