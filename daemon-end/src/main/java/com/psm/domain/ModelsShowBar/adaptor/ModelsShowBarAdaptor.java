package com.psm.domain.ModelsShowBar.adaptor;

import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarDAO;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;

import java.util.List;

public interface ModelsShowBarAdaptor {
    List<ModelsShowBarDAO> selectModelsShowBarOrderByCreateTimeDesc(PageDTO pageDTO);
}
