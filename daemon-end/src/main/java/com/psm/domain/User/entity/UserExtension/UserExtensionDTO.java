package com.psm.domain.User.entity.UserExtension;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExtensionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1507012582578819096L;

    private Long id;
    private short work_num = 0;

    public UserExtensionDTO(Long id) {
        this.id = id;
    }
}
