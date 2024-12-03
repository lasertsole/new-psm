package com.psm.domain.Model.model.repository;

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
     * @param from 启始文档号
     * @param size 每页数量
     * @return 文档分页信息，其中 records 为匹配到的原始记录， total为匹配到的总记录数， size为每页数量, from为启始文档号
     */
    Map<String, Object> selectDetailSearchModel3d(String keyword, Integer from, Integer size) throws IOException;
}
