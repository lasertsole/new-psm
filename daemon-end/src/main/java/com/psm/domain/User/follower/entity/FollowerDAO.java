package com.psm.domain.User.follower.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_followers")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowerDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 328431682623508681L;

    @TableId
    private Long id;

    private Long tgtUserId;
    private Long srcUserId;

    private String createTime;
}
