package com.psm.domain.Model.model.adaptor.impl;

import com.psm.domain.Model.model.adaptor.Model3dAdaptor;
import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.domain.Model.model.types.convertor.Model3dConvertor;
import com.psm.domain.Model.model.service.Model3dService;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.types.enums.VisibleEnum;
import com.psm.utils.Valid.ValidUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.exception.TusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Adaptor
public class Model3dAdaptorImpl implements Model3dAdaptor {
    @Autowired
    ValidUtil validUtil;

    @Autowired
    Model3dService modelService;

    @Override
    public void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException, TusException {
        if(Objects.isNull(servletRequest) || Objects.isNull(servletResponse) || StringUtils.isBlank(userId))
            throw new InvalidParameterException("Invalid parameter");

        modelService.uploadModelEntity(servletRequest, servletResponse, userId);
    }

    @Override
    public Model3dBO uploadModelInfo(@Valid Model3dDTO model3dDTO) throws Exception {
        Long userId = model3dDTO.getUserId();
        String title = model3dDTO.getTitle();
        String content = model3dDTO.getContent();
        MultipartFile cover = model3dDTO.getCover();
        String style = model3dDTO.getStyle();
        String type = model3dDTO.getType();
        Integer visible = model3dDTO.getVisible();

        if (
                Objects.isNull(userId)
                || StringUtils.isBlank(title)
                || StringUtils.isBlank(content)
                || Objects.isNull(cover)
                || StringUtils.isBlank(style)
                || StringUtils.isBlank(type)
                || Objects.isNull(visible)
        )
            throw new InvalidParameterException("Invalid parameter");

        Map<String, Long> map = modelService.uploadModelInfo(userId, title, content, cover, style, type, visible);
        Model3dBO model3dBO = Model3dConvertor.INSTANCE.DTO2BO(model3dDTO);
        model3dBO.setId(map.get("modelId"));
        model3dBO.setStorage(map.get("modelStorage"));

        return model3dBO;
    }

    @Override
    public Model3dBO selectById(@Valid Model3dDTO model3dDTO) throws InvalidParameterException{
        if (
                Objects.isNull(model3dDTO.getId())
                || Objects.isNull(model3dDTO.getVisible())
        )
            throw new InvalidParameterException("Invalid parameter");

        Model3dDAO model3dDAO = modelService.getById(model3dDTO.getId(), VisibleEnum.fromInteger(model3dDTO.getVisible()));

        return Model3dConvertor.INSTANCE.DAO2BO(model3dDAO);
    }

    @Override
    public Model3dBO selectById(Long id, Integer visible) throws InvalidParameterException, InstantiationException, IllegalAccessException {
        if (Objects.isNull(id) || Objects.isNull(visible)) throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", id, "visible", visible), Model3dDTO.class);

        Model3dDAO model3dDAO = modelService.getById(id, VisibleEnum.fromInteger(visible));

        return Model3dConvertor.INSTANCE.DAO2BO(model3dDAO);
    }

    @Override
    public List<Model3dBO> getByUserIds(List<Long> userIds, VisibleEnum visibleEnum) {
        if (Objects.isNull(userIds) || Objects.isNull(visibleEnum)) throw new InvalidParameterException("Invalid parameter");

        List<Model3dDAO> model3dDAOs = modelService.getByUserIds(userIds, visibleEnum);

        return model3dDAOs.stream().map(Model3dConvertor.INSTANCE::DAO2BO).toList();
    }
}
