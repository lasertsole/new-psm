package com.psm.domain.User.entity.User.UserVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -981341206508743904L;

    private Long id; //id保留加速数据库查询(防止回表查询)
    private String name;
    private String avatar;
    private Boolean sex;
    private String profile;
    private String createTime;
}
