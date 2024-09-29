package com.psm.domain.Model.adaptor;

import com.psm.infrastructure.utils.VO.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ModelAdaptor {
    /**
     * 上传模型文件
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    void uploadModelEntity(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) throws IOException;
}
