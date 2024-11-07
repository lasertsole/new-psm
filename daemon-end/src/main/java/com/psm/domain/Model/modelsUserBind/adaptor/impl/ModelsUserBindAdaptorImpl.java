package com.psm.domain.Model.modelsUserBind.adaptor.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Model.model.entity.ModelDTO;
import com.psm.domain.Model.modelsUserBind.adaptor.ModelsUserBindAdaptor;
import com.psm.domain.Model.modelsUserBind.service.ModelsUserBindService;
import com.psm.domain.Model.modelsUserBind.types.convertor.ModelsUserBindConvertor;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindBO;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindDAO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDTO;
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
public class ModelsUserBindAdaptorImpl implements ModelsUserBindAdaptor {
    @Autowired
    ValidUtil validUtil;

    @Autowired
    private ModelsUserBindService modelsUserBindService;

    @Override
    public Page<ModelsUserBindBO> getModelsShowBars(@Valid PageDTO pageDTO, UserExtensionDTO userExtensionDTO, @Valid ModelDTO modelDTO) {
        if(
                Objects.isNull(pageDTO.getCurrent())
                ||Objects.isNull(pageDTO.getSize())
        )
            throw new InvalidParameterException("Invalid parameter");

        Page<ModelsUserBindDAO> modelsUserBindDAOPage = modelsUserBindService.getModelsShowBars(
                pageDTO.getCurrent(), pageDTO.getSize(), userExtensionDTO.getIsIdle(), userExtensionDTO.getCanUrgent(),
                modelDTO.getStyle(), modelDTO.getType(), userExtensionDTO.getId());
        Page<ModelsUserBindBO> modelsUserBindBOPage = new Page<>();
        BeanUtils.copyProperties(modelsUserBindDAOPage, modelsUserBindBOPage);
        List<ModelsUserBindDAO> modelsUserBindDAOs = modelsUserBindDAOPage.getRecords();
        List<ModelsUserBindBO> modelsShowBarsBO = modelsUserBindDAOs.stream().map(ModelsUserBindConvertor.INSTANCE::DAO2BO).toList();
        modelsUserBindBOPage.setRecords(modelsShowBarsBO);

        return modelsUserBindBOPage;
    }

    @Override
    public Page<ModelsUserBindBO> getModelsShowBars(
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
        if (Objects.nonNull(style)) validUtil.validate(Map.of("style", style), ModelDTO.class);
        if (Objects.nonNull(type)) validUtil.validate(Map.of( "type", type), ModelDTO.class);

        Page<ModelsUserBindDAO> modelsUserBindDAOPage = modelsUserBindService.getModelsShowBars(
                current, size, isIdle, canUrgent, style, type, userSelfId);
        Page<ModelsUserBindBO> modelsUserBindBOPage = new Page<>();
        BeanUtils.copyProperties(modelsUserBindDAOPage, modelsUserBindBOPage);
        List<ModelsUserBindDAO> modelsUserBindDAOs = modelsUserBindDAOPage.getRecords();
        List<ModelsUserBindBO> modelsShowBarsBO = modelsUserBindDAOs.stream().map(ModelsUserBindConvertor.INSTANCE::DAO2BO).toList();
        modelsUserBindBOPage.setRecords(modelsShowBarsBO);

        return modelsUserBindBOPage;
    }

    @Override
    public Page<ModelsUserBindBO> getModelsShowBars(
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
