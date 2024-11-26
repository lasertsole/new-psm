package com.psm.domain.Model.models_user.valueObject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.domain.Model.models_user.types.convertor.Models_UserConvertor;
import com.psm.domain.User.user.entity.User.UserDTO;
import lombok.Value;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserDTO implements Serializable {
    UserDTO user;
    List<Model3dDTO> models;

    public static Page<Models_UserDTO> fromBOPage(Page<Models_UserBO> modelsUserBOPage) {
        Page<Models_UserDTO> modelsUserDTOPage = new Page<>();
        BeanUtils.copyProperties(modelsUserBOPage, modelsUserDTOPage);
        modelsUserDTOPage.setRecords(modelsUserBOPage.getRecords().stream().map(Models_UserConvertor.INSTANCE::BO2DTO).toList());

        return modelsUserDTOPage;
    }

    public static Models_UserDTO fromBO(Models_UserBO modelsUserBO) {
        return Models_UserConvertor.INSTANCE.BO2DTO(modelsUserBO);
    }
}