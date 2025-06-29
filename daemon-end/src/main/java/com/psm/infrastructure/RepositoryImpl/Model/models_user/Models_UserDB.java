package com.psm.infrastructure.RepositoryImpl.Model.models_user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Model.Joint.models_user.pojo.valueObject.Models_UserDO;

public interface Models_UserDB {
    /**
     * 获取公开模型Bars展示
     * @param current 当前页码
     * @param size 每页项数
     * @param isIdle 作者是否有空
     * @param canUrgent 作者是否可紧急
     * @param style 模型风格
     * @param type 模型类型
     * @param userSelfId 用户自己的id（查看关注的用户时要用）
     * @return 一页公开模型Bars DO
     */
    Page<Models_UserDO> selectModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId);
}
