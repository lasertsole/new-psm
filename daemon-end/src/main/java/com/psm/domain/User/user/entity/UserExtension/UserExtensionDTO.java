package com.psm.domain.User.user.entity.UserExtension;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
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

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Min(value = 0, message = "The publicModelNum must be greater than or equal to 0")
    private Short publicModelNum;

    @Min(value = 0, message = "The modelCurStorage must be greater than or equal to 0")
    private Long modelCurStorage;

    @Min(value = 0, message = "The modelMaxStorage must be greater than or equal to 0")
    private Long modelMaxStorage;

    private Boolean isIdle;
    private Boolean canUrgent;
    private String createTime;
    private String modifyTime;

    public UserExtensionDTO(Long id) {
        this.id = id;
    }
}
