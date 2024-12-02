package com.psm.domain.Model.model.repository;

import com.psm.domain.Model.model.entity.Model3dDO;
import com.psm.types.common.ES.DO.ESResultDO;
import com.psm.types.common.ES.DO.ESResultPageDO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Model3dES {
    /**
     * 根据模型ID查询简要模型信息
     *
     * @param keyword 关键字
     * @return 模型和高亮字段 的列表
     */
    List<Map<String, Object>> selectBlurSearchModel3d(String keyword) throws IOException;

    /**
     * 根据模型ID查询详细模型信息
     *
     * @param keyword 关键字
     * @return 文档和高亮字段 的分页
     */
    ESResultPageDO selectDetailSearchModel3d(String keyword) throws IOException;
}
