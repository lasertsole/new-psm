package com.psm.domain.Model.models_user.service.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Model.models_user.repository.Models_UserDB;
import com.psm.domain.Model.models_user.service.Models_UserService;
import com.psm.domain.Model.models_user.valueObject.Models_UserBO;
import com.psm.domain.Model.models_user.valueObject.Models_UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Models_UserServiceImpl implements Models_UserService {
    @Autowired
    private Models_UserDB Models_UserDB;

    @Override
    @Cached(name = "modelsShowBars", key = "#current+#size+#isIdle+#canUrgent+#style+#type+#userSelfId", expire = 60, cacheType = CacheType.REMOTE)
    public Page<Models_UserBO> getModelsShowBars(
            Integer current, Integer size, Boolean isIdle, Boolean canUrgent, String style, String type, Long userSelfId) {

        Page<Models_UserDO> modelsUserDOPage = Models_UserDB.selectModelsShowBars(current, size, isIdle, canUrgent, style, type, userSelfId);
        return Models_UserBO.fromDOPage(modelsUserDOPage);
    }
}
