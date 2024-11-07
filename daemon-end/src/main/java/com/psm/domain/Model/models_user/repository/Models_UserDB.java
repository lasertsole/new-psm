package com.psm.domain.Model.models_user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Model.models_user.valueObject.Models_UserDAO;

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
     * @return 一页公开模型Bars DAO
     */
    Page<Models_UserDAO> selectModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId);
}
