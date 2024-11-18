package com.psm.infrastructure.DB;

import com.psm.domain.Subtitles.entity.SubtitlesDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubtitlesMapper extends BaseDBMapper<SubtitlesDO> {
}
