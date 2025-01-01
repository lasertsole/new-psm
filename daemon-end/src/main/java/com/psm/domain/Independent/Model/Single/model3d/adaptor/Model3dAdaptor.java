package com.psm.domain.Independent.Model.Single.model3d.adaptor;

import com.psm.domain.Independent.Model.Single.model3d.pojo.entity.Model3dBO;
import com.psm.types.enums.VisibleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.desair.tus.server.exception.TusException;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

public interface Model3dAdaptor {
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
     * @param model3dBO  模型信息
     * @throws TusException    tus异常
     * @throws IOException     IO异常
     */
    void uploadModelInfo(Model3dBO model3dBO) throws Exception;

    /**
     * 根据模型ID查询模型
     *
     * @param model3dBO  模型信息
     * @return 模型BO
     */
    Model3dBO selectById(Model3dBO model3dBO);

    /**
     * 根据模型ID查询模型
     *
     * @param id        模型ID
     * @param visible   可见性
     * @return 模型BO
     * @throws InvalidParameterException   参数无效异常
     * @throws InstantiationException      实例化异常
     * @throws IllegalAccessException      非法访问异常
     */
    Model3dBO selectById(Long id, Integer visible) throws InvalidParameterException, InstantiationException, IllegalAccessException;

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
     * @return 模型id和高亮字段 的列表
     */
    List<Map<String, Object>> getBlurSearchModel3d(String keyword) throws IOException;
    /**
     * 滚动查询详细模型信息
     *
     * @param keyword 关键字
     * @param afterKeyId 深分页的afterKeyId
     * @param size 每页数量
     * @return 文档分页信息，其中 records 为匹配到的原始记录， total为匹配到的总记录数， size为每页数量, next_after_key为下一页的afterKeyId
     * @throws IOException IO异常
     */
    Map<String, Object> getDetailSearchModel3d(String keyword, String afterKeyId, Integer size) throws IOException;
}
