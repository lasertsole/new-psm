package com.psm.domain.Model.models_user.valueObject;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.models_user.types.convertor.Models_UserConvertor;
import com.psm.domain.User.user.entity.User.UserBO;
import lombok.Value;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Models_UserBO implements Serializable {
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
}
