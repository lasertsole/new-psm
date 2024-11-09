package com.psm.domain.Model.models_user.adaptor.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.domain.Model.models_user.adaptor.Models_UserAdaptor;
import com.psm.domain.Model.models_user.service.Models_UserService;
import com.psm.domain.Model.models_user.types.convertor.Models_UserConvertor;
import com.psm.domain.Model.models_user.valueObject.Models_UserBO;
import com.psm.domain.Model.models_user.valueObject.Models_UserDAO;
import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.types.utils.Valid.ValidUtil;
import com.psm.types.utils.page.PageDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Adaptor
public class Models_UserAdaptorImpl implements Models_UserAdaptor {
    @Autowired
    ValidUtil validUtil;

    @Autowired
    private Models_UserService models_UserService;

    @Override
    public Page<Models_UserBO> getModelsShowBars(@Valid PageDTO pageDTO, UserDTO userDTO, @Valid Model3dDTO modelDTO) {
        if(
                Objects.isNull(pageDTO.getCurrent())
                ||Objects.isNull(pageDTO.getSize())
        )
            throw new InvalidParameterException("Invalid parameter");

        Page<Models_UserDAO> modelsUserBindDAOPage = models_UserService.getModelsShowBars(
                pageDTO.getCurrent(), pageDTO.getSize(), userDTO.getIsIdle(), userDTO.getCanUrgent(),
                modelDTO.getStyle(), modelDTO.getType(), userDTO.getId());
        Page<Models_UserBO> modelsUserBindBOPage = new Page<>();
        BeanUtils.copyProperties(modelsUserBindDAOPage, modelsUserBindBOPage);
        List<Models_UserDAO> modelsUserBindDAOs = modelsUserBindDAOPage.getRecords();
        List<Models_UserBO> modelsShowBarsBO = modelsUserBindDAOs.stream().map(Models_UserConvertor.INSTANCE::DAO2BO).toList();
        modelsUserBindBOPage.setRecords(modelsShowBarsBO);

        return modelsUserBindBOPage;
    }

    @Override
    public Page<Models_UserBO> getModelsShowBars(
            Integer current,
            Integer size,
            Boolean isIdle,
            Boolean canUrgent,
            String style,
            String type,
            Long userSelfId
    ) throws InstantiationException, IllegalAccessException {
        if(
                Objects.isNull(current)
                ||Objects.isNull(size)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("current", current, "size", size), PageDTO.class);
        if (Objects.nonNull(style)) validUtil.validate(Map.of("style", style), Model3dDTO.class);
        if (Objects.nonNull(type)) validUtil.validate(Map.of( "type", type), Model3dDTO.class);

        Page<Models_UserDAO> modelsUserBindDAOPage = models_UserService.getModelsShowBars(
                current, size, isIdle, canUrgent, style, type, userSelfId);
        Page<Models_UserBO> modelsUserBindBOPage = new Page<>();
        BeanUtils.copyProperties(modelsUserBindDAOPage, modelsUserBindBOPage);
        List<Models_UserDAO> modelsUserBindDAOs = modelsUserBindDAOPage.getRecords();
        List<Models_UserBO> modelsShowBarsBO = modelsUserBindDAOs.stream().map(Models_UserConvertor.INSTANCE::DAO2BO).toList();
        modelsUserBindBOPage.setRecords(modelsShowBarsBO);

        return modelsUserBindBOPage;
    }

    @Override
    public Page<Models_UserBO> getModelsShowBars(
            Integer current,
            Integer size,
            Boolean isIdle,
            Boolean canUrgent,
            String style,
            String type
    ) throws InstantiationException, IllegalAccessException {
        return getModelsShowBars(current, size, isIdle, canUrgent, style, type, null);
    }
}
