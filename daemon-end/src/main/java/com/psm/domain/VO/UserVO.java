package com.psm.domain.VO;

import com.psm.enums.SexEnum;

import java.io.Serializable;

public class UserVO implements Serializable {
    private static final long serialVersionUID = 6088280415008121003L;

    private Long id; //id保留加速数据库查询(防止回表查询)
    private String name;
    private String avatar;
    private SexEnum sex;
    private String profile;
    private String createTime;
}
