package com.psm.domain.Model.modelShowPage.valueObject;

import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.User.entity.User.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelShowPageDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6864546604457683153L;

    private UserDAO user;
    private ModelDAO model;
}
