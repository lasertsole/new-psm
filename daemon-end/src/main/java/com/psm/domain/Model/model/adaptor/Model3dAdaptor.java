package com.psm.domain.Model.model.adaptor;

import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.types.enums.VisibleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.desair.tus.server.exception.TusException;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

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
     * @return 模型BO实体
     */
    Model3dBO uploadModelInfo(Model3dBO model3dBO) throws Exception;

    /**
     * 根据模型ID查询模型
     *
     * @param model3dDTO  模型信息
     * @return 模型BO
     */
    Model3dBO selectById(Model3dDTO model3dDTO);

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
}
