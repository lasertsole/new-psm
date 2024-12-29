package com.psm.infrastructure.DB;

import com.psm.domain.Independent.Communication.Single.Chat.entity.ChatDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper extends BaseDBMapper<ChatDO> {
}
