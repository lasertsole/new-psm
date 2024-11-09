package com.psm.infrastructure.DB;

import com.github.yulichang.base.MPJBaseMapper;
import com.psm.domain.Chat.entity.ChatDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper extends MPJBaseMapper<ChatDAO> {
}
