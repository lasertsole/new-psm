package com.psm.domain.Model.modelsShowBar.valueObject;

import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.User.entity.User.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelsShowBarDAO implements Serializable { // DAO实体不具有值对象性质，可以通过set方法赋值，方便快速构建
    @Serial
    private static final long serialVersionUID = -5702851258168897074L;

    private UserDAO user;
    private List<ModelDAO> models;
}
