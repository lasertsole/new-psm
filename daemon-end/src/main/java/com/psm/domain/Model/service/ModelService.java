package com.psm.domain.Model.service;

import com.psm.domain.Model.entity.ModelDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.desair.tus.server.exception.TusException;

import java.io.IOException;

public interface ModelService {
    /**
     * 上传模型文件
     *
     * @param servletRequest 浏览器请求
     * @param servletResponse 服务器响应
     * @param userId 用户id
     */
    void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException, TusException;

    /**
     * 上传模型信息
     *
     * @param modelDTO 模型DTO对象, 包括本地模型文件路径和模型信息
     * @throws TusException tus异常
     * @throws IOException io异常
     */
    void uploadModelInfo(ModelDTO modelDTO) throws Exception;

    /**
     * 删除模型信息
     *
     * @param modelDTO 模型DTO对象, 包括模型id
     * @throws IOException io异常
     */
    void removeModelInfo(ModelDTO modelDTO) throws IOException;
}
