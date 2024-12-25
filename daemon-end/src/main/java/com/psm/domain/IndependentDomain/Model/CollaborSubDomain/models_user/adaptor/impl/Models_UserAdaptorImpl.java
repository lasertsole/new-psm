package com.psm.domain.IndependentDomain.Model.CollaborSubDomain.models_user.adaptor.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.IndependentDomain.Model.model.entity.Model3dDTO;
import com.psm.domain.IndependentDomain.Model.CollaborSubDomain.models_user.adaptor.Models_UserAdaptor;
import com.psm.domain.IndependentDomain.Model.CollaborSubDomain.models_user.service.Models_UserService;
import com.psm.domain.IndependentDomain.Model.CollaborSubDomain.models_user.valueObject.Models_UserBO;
import com.psm.domain.IndependentDomain.User.user.entity.User.UserDTO;
import com.psm.utils.Valid.ValidUtil;
import com.psm.utils.page.PageBO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;
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
    public Page<Models_UserBO> getModelsShowBars(@Valid PageBO pageBO, UserDTO userDTO, @Valid Model3dDTO model3dDTO) {
        if(
                Objects.isNull(pageBO.getCurrent())
                ||Objects.isNull(pageBO.getSize())
        )
            throw new InvalidParameterException("Invalid parameter");

        return models_UserService.getModelsShowBars(
                pageBO.getCurrent(), pageBO.getSize(), userDTO.getIsIdle(), userDTO.getCanUrgent(),
                model3dDTO.getStyle(), model3dDTO.getType(), Long.parseLong(userDTO.getId()));
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

        validUtil.validate(Map.of("current", current, "size", size), PageBO.class);
        if (Objects.nonNull(style)) validUtil.validate(Map.of("style", style), Model3dDTO.class);
        if (Objects.nonNull(type)) validUtil.validate(Map.of( "type", type), Model3dDTO.class);

        return models_UserService.getModelsShowBars(
                current, size, isIdle, canUrgent, style, type, userSelfId);
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
