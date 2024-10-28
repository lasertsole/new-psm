package com.psm.domain.Model.modelsShowBar.adaptor;

import com.psm.domain.Model.modelsShowBar.valueObject.ModelsShowBarBO;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;

import java.util.List;

public interface ModelsShowBarAdaptor {

    List<ModelsShowBarBO> selectModelsShowBarOrderByCreateTimeDesc(PageDTO pageDTO);
}
