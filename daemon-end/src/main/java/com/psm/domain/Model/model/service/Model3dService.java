package com.psm.domain.Model.model.service;

import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.types.common.ES.BO.ESResultBO;
import com.psm.types.common.ES.BO.ESResultPageBO;
import com.psm.types.enums.VisibleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.desair.tus.server.exception.TusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Model3dService {
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
     * @param userId    用户ID
     * @param title     模型标题
     * @param content   模型内容
     * @param coverFile 封面文件
     * @param style     模型风格
     * @param type      模型类型
     * @param visible   可见性
     * @throws TusException tus异常
     * @throws IOException io异常
     */
    void uploadModelInfo(Long userId, String title, String content, MultipartFile coverFile, String style, String type, VisibleEnum visible) throws Exception;

    /**
     * 删除模型信息
     *
     * @param Id 模型id
     * @throws IOException io异常
     */
    void removeModelInfo(Long Id) throws IOException;

    /**
     * 根据模型ID查询模型
     *
     * @param modelId 模型ID
     * @param visibleEnum 可见性等级枚举
     * @return 模型BO
     */
    Model3dBO getById(Long modelId, VisibleEnum visibleEnum);

    /**
     * 根据模型ID查询模型
     *
     * @param userIds 用户ID列表
     * @param visibleEnum 可见性等级枚举
     * @return 模型BO
     */
    List<Model3dBO> getByUserIds(List<Long> userIds, VisibleEnum visibleEnum);

    /**
     * 根据模型ID查询简要模型信息
     *
     * @param keyword 关键字
     * @return 模型和高亮字段 的列表
     */
    List<Map<String, Object>> getBlurSearchModel3d(String keyword) throws IOException;

    /**
     * 根据模型ID查询详细模型信息
     *
     * @param keyword 关键字
     * @return 文档和高亮字段 的分页
     */
    ESResultPageBO getDetailSearchModel3d(String keyword) throws IOException;
}
