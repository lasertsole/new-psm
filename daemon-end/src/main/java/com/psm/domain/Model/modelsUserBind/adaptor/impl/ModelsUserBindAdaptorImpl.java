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
import com.psm.types.utils.page.PageDTO;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Adaptor
public class ModelsUserBindAdaptorImpl implements ModelsUserBindAdaptor {
    @Autowired
    private ModelsUserBindService modelsUserBindService;

    @Override
    public Page<ModelsUserBindBO> getModelsShowBars(@Valid PageDTO pageDTO, @Valid UserExtensionDTO userExtensionDTO, @Valid ModelDTO modelDTO) {
        if(
                Objects.isNull(pageDTO.getCurrent())
                ||Objects.isNull(pageDTO.getSize())
        )
            throw new InvalidParameterException("Invalid parameter");

        Page<ModelsUserBindDAO> modelsUserBindDAOPage = modelsUserBindService.getModelsShowBars(
                pageDTO.getCurrent(), pageDTO.getSize(), userExtensionDTO.getIsIdle(), userExtensionDTO.getCanUrgent(), modelDTO.getStyle(), modelDTO.getType());
        Page<ModelsUserBindBO> modelsUserBindBOPage = new Page<>();
        BeanUtils.copyProperties(modelsUserBindDAOPage, modelsUserBindBOPage);
        List<ModelsUserBindDAO> modelsUserBindDAOs = modelsUserBindDAOPage.getRecords();
        List<ModelsUserBindBO> modelsShowBarsBO = modelsUserBindDAOs.stream().map(ModelsUserBindConvertor.INSTANCE::DAO2BO).toList();
        modelsUserBindBOPage.setRecords(modelsShowBarsBO);

        return modelsUserBindBOPage;
    }
}
