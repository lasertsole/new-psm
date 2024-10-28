package com.psm.domain.Model.model.service;

import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.model.entity.ModelDTO;
import com.psm.infrastructure.enums.VisibleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.desair.tus.server.exception.TusException;

import java.io.IOException;
import java.util.Map;

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
     * @return Map,其中modelId为模型id, modelStorage为模型大小
     */
    Map<String, Long> uploadModelInfo(ModelDTO modelDTO) throws Exception;

    /**
     * 删除模型信息
     *
     * @param modelDTO 模型DTO对象, 包括模型id
     * @throws IOException io异常
     */
    void removeModelInfo(ModelDTO modelDTO) throws IOException;

    /**
     * 根据模型ID查询模型
     *
     * @param modelId 模型ID
     * @param visibleEnum 可见性等级枚举
     * @return 模型DAO
     */
    ModelDAO selectById(Long modelId, VisibleEnum visibleEnum);
}
