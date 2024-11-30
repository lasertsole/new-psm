package com.psm.domain.Model.model.repository;

import com.psm.domain.Model.model.entity.Model3dBO;

import java.util.List;

public interface Model3dES {
    /**
     * 根据模型ID查询简要模型信息
     *
     * @param keyword 关键字
     * @return 模型BO列表
     */
    List<Model3dBO> selectBlurSearchModel3d(String keyword);

    /**
     * 根据模型ID查询详细模型信息
     *
     * @param keyword 关键字
     * @return 模型BO列表
     */
    List<Model3dBO> selectDetailSearchModel3d(String keyword);
}
