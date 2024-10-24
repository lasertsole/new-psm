package com.psm.domain.Model.adaptor;

import com.psm.domain.Model.entity.ModelBO;
import com.psm.domain.Model.entity.ModelDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.desair.tus.server.exception.TusException;

import java.io.IOException;

public interface ModelAdaptor {
    /**
     * 上传模型文件
     *
     * @param servletRequest   http请求
     * @param servletResponse  http响应
     * @param userId           用户id
     * @throws IOException     IO异常
     * @throws TusException    tus异常
     */
    void uploadModelEntity(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse, String userId) throws IOException, TusException;

    /**
     * 上传模型信息
     *
     * @param modelDTO  模型信息
     * @throws TusException    tus异常
     * @throws IOException     IO异常
     * @return 模型BO实体
     */
    ModelBO uploadModelInfo(ModelDTO modelDTO) throws Exception;

    /**
     * 根据模型ID查询模型
     *
     * @param modelDTO  模型信息
     * @return 模型BO
     */
    ModelBO selectById(ModelDTO modelDTO);
}
