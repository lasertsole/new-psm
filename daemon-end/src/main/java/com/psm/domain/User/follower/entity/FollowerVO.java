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
    private static final long serialVersionUID = -8444683690763097007L;

    private Long id;

    private Long tgtUserId;
    private Long srcUserId;

    private String createTime;
}
