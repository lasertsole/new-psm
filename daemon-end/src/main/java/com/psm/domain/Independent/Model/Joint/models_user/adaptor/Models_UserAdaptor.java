package com.psm.domain.Independent.Model.Joint.models_user.adaptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Model.Single.model3d.pojo.entity.Model3dDTO;
import com.psm.domain.Independent.Model.Joint.models_user.pojo.valueObject.Models_UserBO;
import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserDTO;
import com.psm.utils.page.PageBO;

public interface Models_UserAdaptor {
    /**
     * 获取公开模型Bars展示
     * @param pageDTO 页DTO
     * @param userDTO 用户DTO
     * @param model3dDTO 模型DTO
     * @return 一页公开模型Bars DAO
     */
    Page<Models_UserBO> getModelsShowBars(PageBO pageDTO, UserDTO userDTO, Model3dDTO model3dDTO);

    /**
     * 获取公开模型Bars展示
     * @param current 当前页
     * @param size 每页项数
     * @param isIdle 是否空闲
     * @param canUrgent 是否可紧急
     * @param style 模型风格
     * @param type 模型类型
     * @param userSelfId 用户自己的id（查看关注的用户时要用）
     * @return 一页公开模型Bars DAO
     */
    Page<Models_UserBO> getModelsShowBars (
        Integer current,
        Integer size,
        Boolean isIdle,
        Boolean canUrgent,
        String style,
        String type,
        Long userSelfId
    ) throws InstantiationException, IllegalAccessException;

    /**
     * 获取公开模型Bars展示 (无UserSelfId，不进行关注筛选)
     * @param current 当前页
     * @param size 每页项数
     * @param isIdle 是否空闲
     * @param canUrgent 是否可紧急
     * @param style 模型风格
     * @param type 模型类型
     * @return 一页公开模型Bars DAO
     */
    Page<Models_UserBO> getModelsShowBars (
            Integer current,
            Integer size,
            Boolean isIdle,
            Boolean canUrgent,
            String style,
            String type
    ) throws InstantiationException, IllegalAccessException;
}
