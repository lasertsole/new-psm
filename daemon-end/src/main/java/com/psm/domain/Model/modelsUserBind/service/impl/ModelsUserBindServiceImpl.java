package com.psm.domain.Model.modelsUserBind.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Model.modelsUserBind.repository.ModelsUserBindDB;
import com.psm.domain.Model.modelsUserBind.service.ModelsUserBindService;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModelsUserBindServiceImpl implements ModelsUserBindService {
    @Autowired
    private ModelsUserBindDB modelsUserBindDB;

    @Override
    public Page<ModelsUserBindDAO> getModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type) {
        return modelsUserBindDB.selectModelsShowBars(current, size, isIdle, canUrgent, style, type);
    }
}
