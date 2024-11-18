package com.psm.domain.Subtitles.types.convertor;

import com.psm.domain.Subtitles.entity.SubtitlesDO;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.domain.Subtitles.entity.SubtitlesVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper
public abstract class SubtitlesConvertor {
    public static SubtitlesConvertor INSTANCE = Mappers.getMapper(SubtitlesConvertor.class);


    public SubtitlesDO DTO2DO(SubtitlesDTO subtitlesDTO){
        SubtitlesDO subtitlesDO = new SubtitlesDO();
        BeanUtils.copyProperties(subtitlesDTO, subtitlesDO);

        return subtitlesDO;
    }

    public SubtitlesVO DO2VO(SubtitlesDO subtitlesDO){
        SubtitlesVO subtitlesVO = new SubtitlesVO();
        BeanUtils.copyProperties(subtitlesDO, subtitlesVO);
        subtitlesVO.setStyle(subtitlesDO.getStyle());
        subtitlesVO.setType(subtitlesDO.getType());

        return subtitlesVO;
    }
}
