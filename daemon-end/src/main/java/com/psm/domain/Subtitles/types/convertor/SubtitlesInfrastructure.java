package com.psm.domain.Subtitles.types.convertor;

import com.psm.domain.Subtitles.entity.SubtitlesDAO;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.domain.Subtitles.entity.SubtitlesVO;
import org.springframework.beans.BeanUtils;

public class SubtitlesInfrastructure {

    public static SubtitlesDAO DTOConvertToDAO(SubtitlesDTO subtitlesDTO){
        SubtitlesDAO subtitlesDAO = new SubtitlesDAO();
        BeanUtils.copyProperties(subtitlesDTO, subtitlesDAO);

        return subtitlesDAO;
    }

    public static SubtitlesVO DAOConvertToVO(SubtitlesDAO subtitlesDAO){
        SubtitlesVO subtitlesVO = new SubtitlesVO();
        BeanUtils.copyProperties(subtitlesDAO, subtitlesVO);
        subtitlesVO.setCategory(subtitlesDAO.getCategory().toString());

        return subtitlesVO;
    }
}
