package com.psm.domain.Independent.Model.Joint.models_user.valueObject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dDTO;
import com.psm.domain.Independent.Model.Joint.models_user.types.convertor.Models_UserConvertor;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDTO;
import com.psm.types.common.BO.BO;
import com.psm.types.common.DTO.DTO;
import lombok.Value;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserDTO implements Serializable, DTO {
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

    @Override
    public BO toBO() {
        return Models_UserConvertor.INSTANCE.DTO2BO(this);
    }
}