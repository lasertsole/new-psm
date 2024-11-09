package com.psm.infrastructure.DB;

import com.github.yulichang.base.MPJBaseMapper;
import com.psm.domain.Subtitles.entity.SubtitlesDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubtitlesMapper extends MPJBaseMapper<SubtitlesDAO> {
}
