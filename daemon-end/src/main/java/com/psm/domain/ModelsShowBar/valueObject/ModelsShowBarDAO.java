package com.psm.domain.ModelsShowBar.valueObject;

import com.psm.domain.Model.entity.ModelDAO;
import com.psm.domain.User.entity.User.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelsShowBarDAO {
    private UserDAO userDAO;
    private List<ModelDAO> modelDAOs;
}
