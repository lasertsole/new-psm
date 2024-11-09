package com.psm.domain.Subtitles.types.convertor;

import com.psm.domain.Subtitles.entity.SubtitlesDAO;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.domain.Subtitles.entity.SubtitlesVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

@Mapper
public abstract class SubtitlesConvertor {
    public static SubtitlesConvertor INSTANCE = Mappers.getMapper(SubtitlesConvertor.class);


    public SubtitlesDAO DTO2DAO(SubtitlesDTO subtitlesDTO){
        SubtitlesDAO subtitlesDAO = new SubtitlesDAO();
        BeanUtils.copyProperties(subtitlesDTO, subtitlesDAO);

        return subtitlesDAO;
    }

    public SubtitlesVO DAO2VO(SubtitlesDAO subtitlesDAO){
        SubtitlesVO subtitlesVO = new SubtitlesVO();
        BeanUtils.copyProperties(subtitlesDAO, subtitlesVO);
        subtitlesVO.setStyle(subtitlesDAO.getStyle());
        subtitlesVO.setType(subtitlesDAO.getType());

        return subtitlesVO;
    }
}
