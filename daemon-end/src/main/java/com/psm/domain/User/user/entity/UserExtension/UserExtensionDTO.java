package com.psm.domain.User.user.entity.UserExtension;

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
public class UserExtensionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7739665258774712512L;

    private Long id;
    private Short publicModelNum;
    private Long modelCurStorage;
    private Long modelMaxStorage;
    private Boolean isIdle;
    private Boolean canUrgent;
    private String createTime;
    private String modifyTime;

    public UserExtensionDTO(Long id) {
        this.id = id;
    }
}
