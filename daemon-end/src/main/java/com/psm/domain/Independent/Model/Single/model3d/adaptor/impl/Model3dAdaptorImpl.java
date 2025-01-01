package com.psm.domain.Independent.Model.Single.model3d.adaptor.impl;

import com.psm.domain.Independent.Model.Single.model3d.adaptor.Model3dAdaptor;
import com.psm.domain.Independent.Model.Single.model3d.pojo.entity.Model3dBO;
import com.psm.domain.Independent.Model.Single.model3d.service.Model3dService;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.infrastructure.Tus.Tus;
import com.psm.types.enums.VisibleEnum;
import com.psm.utils.Long.LongUtils;
import com.psm.utils.Valid.ValidUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.exception.TusException;
import org.apache.commons.text.StringEscapeUtils;
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
    Tus tus;

    @Autowired
    ValidUtil validUtil;

    @Autowired
    Model3dService modelService;

    @Override
    public void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException, TusException {
        String folderName = tus.getFolderName(servletRequest);
        if(Objects.isNull(servletRequest)
                || Objects.isNull(servletResponse)
                || StringUtils.isBlank(userId)
        )
            throw new InvalidParameterException("Invalid parameter");

        modelService.uploadModelEntity(servletRequest, servletResponse, userId);
    }

    @Override
    public void uploadModelInfo(Model3dBO model3dBO) throws Exception {
        Long userId = model3dBO.getUserId();
        String title = StringEscapeUtils.escapeHtml4(model3dBO.getTitle());
        String content = StringEscapeUtils.escapeHtml4(model3dBO.getContent());
        MultipartFile CoverFile = model3dBO.getCoverFile();
        String style = StringEscapeUtils.escapeHtml4(model3dBO.getStyle());
        String type = StringEscapeUtils.escapeHtml4(model3dBO.getType());
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

        validUtil.validate(Map.of("title", title, "content", content, "cover", StringEscapeUtils.escapeHtml4(CoverFile.getOriginalFilename()), "style", style, "type", type), Model3dBO.class);

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

        validUtil.validate(Map.of("id", id, "visible", visible), Model3dBO.class);

        return modelService.getById(id, VisibleEnum.fromInteger(visible));
    }

    @Override
    public List<Model3dBO> getByUserIds(List<Long> userIds, VisibleEnum visibleEnum) {
        if (Objects.isNull(userIds) || Objects.isNull(visibleEnum)) throw new InvalidParameterException("Invalid parameter");

        return modelService.getByUserIds(userIds, visibleEnum);
    }

    @Override
    public List<Map<String, Object>> getBlurSearchModel3d(String keyword) throws IOException {
        keyword = StringEscapeUtils.escapeHtml4(keyword);
        if (StringUtils.isBlank(keyword)) throw new InvalidParameterException("Invalid parameter");

        return modelService.getBlurSearchModel3d(keyword);
    }

    @Override
    public Map<String, Object> getDetailSearchModel3d(String keyword, String afterKeyId, Integer size) throws IOException {
        keyword = StringEscapeUtils.escapeHtml4(keyword);
        if (
                StringUtils.isBlank(keyword)
                && Objects.isNull(size)
                && (size < 10 || size > 50)
        ) throw new InvalidParameterException("Invalid parameter");

        return modelService.getDetailSearchModel3d(keyword, LongUtils.stringConvertedToLong(afterKeyId), size);
    }
}
