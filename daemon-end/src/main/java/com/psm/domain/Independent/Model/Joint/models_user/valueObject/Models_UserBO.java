package com.psm.domain.Independent.Model.Joint.models_user.valueObject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dBO;
import com.psm.domain.Independent.Model.Joint.models_user.types.convertor.Models_UserConvertor;
import com.psm.domain.Independent.User.Single.user.entity.User.UserBO;
import com.psm.types.common.BO.BO;
import lombok.Value;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserBO implements Serializable, BO<Models_UserDTO, Models_UserDO> {
    UserBO user;
    List<Model3dBO> models;

    public static Page<Models_UserBO> fromDOPage(Page<Models_UserDO> modelsUserDOPage) {
        Page<Models_UserBO> modelsUserBOPage = new Page<>();
        BeanUtils.copyProperties(modelsUserDOPage, modelsUserBOPage);
        modelsUserBOPage.setRecords(modelsUserDOPage.getRecords().stream().map(Models_UserConvertor.INSTANCE::DO2BO).toList());

        return modelsUserBOPage;
    }

    public static Models_UserBO fromDO(Models_UserDO modelsUserDO) {
        return Models_UserConvertor.INSTANCE.DO2BO(modelsUserDO);
    }

    @Override
    public Models_UserDTO toDTO() {
        return Models_UserConvertor.INSTANCE.BO2DTO(this);
    }

    @Override
    public Models_UserDO toDO() {
        return Models_UserConvertor.INSTANCE.BO2DO(this);
    }
}
