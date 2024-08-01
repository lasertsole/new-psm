package com.psm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -4678091231437634330L;
    @TableId
    private Long id;
    private String name;
    private String password;
    private String phone;
    private String email;
    private String createTime;
    private String modifyTime;
}
