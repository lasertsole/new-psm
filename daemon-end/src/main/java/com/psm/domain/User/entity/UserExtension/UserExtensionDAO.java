package com.psm.domain.User.entity.UserExtension;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_users_extension")
public class UserExtensionDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3628427399383031148L;

    @TableId
    private Long id;
    private Short modelNum;
    private Long modelCurStorage;
    private Long modelMaxStorage;
    private String createTime;
    private String modifyTime;

    @Version
    private Integer version;

    public UserExtensionDAO(Long id) {
        this.id = id;
    }
}
