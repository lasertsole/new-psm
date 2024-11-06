package com.psm.domain.Model.modelsUserBind.adaptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Model.model.entity.ModelDTO;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindBO;
import com.psm.types.utils.page.PageDTO;

public interface ModelsUserBindAdaptor {
    /**
     * 获取公开模型Bars展示
     * @param pageDTO 页DTO
     * @param modelDTO 模型DTO
     * @return 一页公开模型Bars DAO
     */
    Page<ModelsUserBindBO> getModelsShowBars(PageDTO pageDTO, ModelDTO modelDTO);
}
