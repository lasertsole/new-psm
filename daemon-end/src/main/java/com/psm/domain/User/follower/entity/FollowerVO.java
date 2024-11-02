package com.psm.domain.User.follower.entity;

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
public class FollowerVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8040996100086553973L;

    private String id;

    private String tgtUserId;
    private String srcUserId;

    private String createTime;
}
