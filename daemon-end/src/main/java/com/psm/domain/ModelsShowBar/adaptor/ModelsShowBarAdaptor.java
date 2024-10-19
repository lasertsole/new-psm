package com.psm.domain.ModelsShowBar.adaptor;

import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarBO;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;

import java.util.List;

public interface ModelsShowBarAdaptor {

    List<ModelsShowBarBO> selectModelsShowBarOrderByCreateTimeDesc(PageDTO pageDTO);
}
