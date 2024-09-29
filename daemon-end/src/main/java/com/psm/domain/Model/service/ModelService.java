package com.psm.domain.Model.service;

import com.psm.infrastructure.utils.VO.ResponseVO;
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
     */
    void uploadModelEntity(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException;

    /**
     * 上传模型信息
     *
     * @param EntityUrl 模型实体在本地的路径
     * @return 模型和封面在阿里云oss中的路径
     * @throws TusException
     * @throws IOException
     */
    String uploadModelInfo(String EntityUrl) throws TusException, IOException;
}
