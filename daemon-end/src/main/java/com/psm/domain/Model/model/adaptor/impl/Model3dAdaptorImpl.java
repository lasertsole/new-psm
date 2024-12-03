package com.psm.domain.Model.model.adaptor.impl;

import com.alibaba.fastjson2.JSONObject;
import com.psm.domain.Model.model.adaptor.Model3dAdaptor;
import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model.entity.Model3dDTO;
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
import java.util.HashMap;
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
    public void uploadModelInfo(@Valid Model3dBO model3dBO) throws Exception {
        Long userId = model3dBO.getUserId();
        String title = model3dBO.getTitle();
        String content = model3dBO.getContent();
        MultipartFile CoverFile = model3dBO.getCoverFile();
        String style = model3dBO.getStyle();
        String type = model3dBO.getType();
        VisibleEnum visible = model3dBO.getVisible();

        if (
            Objects.isNull(userId)
            || StringUtils.isBlank(title)
            || StringUtils.isBlank(content)
            || Objects.isNull(CoverFile)
            || StringUtils.isBlank(style)
            || StringUtils.isBlank(type)
            || Objects.isNull(visible)
        )
            throw new InvalidParameterException("Invalid parameter");

        modelService.uploadModelInfo(userId, title, content, CoverFile, style, type, visible);
    }

    @Override
    public Model3dBO selectById(@Valid Model3dBO model3dBO) throws InvalidParameterException{
        if (
                Objects.isNull(model3dBO.getId())
                || Objects.isNull(model3dBO.getVisible())
        )
            throw new InvalidParameterException("Invalid parameter");

        return modelService.getById(model3dBO.getId(), model3dBO.getVisible());
    }

    @Override
    public Model3dBO selectById(Long id, Integer visible) throws InvalidParameterException, InstantiationException, IllegalAccessException {
        if (Objects.isNull(id) || Objects.isNull(visible)) throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", id, "visible", visible), Model3dDTO.class);

        return modelService.getById(id, VisibleEnum.fromInteger(visible));
    }

    @Override
    public List<Model3dBO> getByUserIds(List<Long> userIds, VisibleEnum visibleEnum) {
        if (Objects.isNull(userIds) || Objects.isNull(visibleEnum)) throw new InvalidParameterException("Invalid parameter");

        return modelService.getByUserIds(userIds, visibleEnum);
    }

    @Override
    public List<Map<String, Object>> getBlurSearchModel3d(String keyword) throws IOException {
        if (StringUtils.isBlank(keyword)) throw new InvalidParameterException("Invalid parameter");

        return modelService.getBlurSearchModel3d(keyword);
    }

    @Override
    public Map<String, Object> getDetailSearchModel3d(String keyword, Integer current, Integer size) throws IOException {
        if (
            StringUtils.isBlank(keyword)
            && Objects.isNull(current)
            && current <= 0
            && Objects.isNull(size)
            && (size < 10 || size > 50)
        ) throw new InvalidParameterException("Invalid parameter");

        return modelService.getDetailSearchModel3d(keyword, current, size);
    }
}
