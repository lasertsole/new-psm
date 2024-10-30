package com.psm.domain.User.user.entity.User.UserVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5426179747086031299L;

    private String id; //id保留加速数据库查询(防止回表查询)
    private String name;
    private String avatar;
    private Boolean sex;
    private String profile;
    private String createTime;
}
