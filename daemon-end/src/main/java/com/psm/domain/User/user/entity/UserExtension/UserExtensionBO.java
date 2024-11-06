package com.psm.domain.User.user.entity.UserExtension;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserExtensionBO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8557931650212377888L;

    private Long id;
    private Short publicModelNum;
    private Long modelCurStorage;
    private Long modelMaxStorage;
    private Boolean isIdle;
    private Boolean canUrgent;
    private String createTime;
    private String modifyTime;

    public UserExtensionBO(Long id) {
        this.id = id;
    }

    public static List<Long> getModelIds(List<UserExtensionBO> userExtensionBOs) {
        return userExtensionBOs.stream().map(UserExtensionBO::getId).toList();
    }
}
