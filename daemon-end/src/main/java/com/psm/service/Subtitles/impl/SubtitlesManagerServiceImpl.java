package com.psm.service.Subtitles.impl;

import com.psm.domain.Subtitles.SubtitlesDAO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.Subtitles.SubtitlesManagerService;
import com.psm.service.Subtitles.SubtitlesService;
import org.springframework.beans.factory.annotation.Autowired;

public class SubtitlesManagerServiceImpl implements SubtitlesManagerService {
    @Autowired
    private SubtitlesService subtitlesService;

    @Override
    public ResponseDTO getSubtitlesById(Long id) {
        return subtitlesService.getSubtitlesById(id);
    }

    @Override
    public ResponseDTO getSubtitlesListByPage(Integer currentPage, Integer pageSize) {
        return subtitlesService.getSubtitlesListByPage(currentPage, pageSize);
    }

    @Override
    public ResponseDTO addSubtitles(SubtitlesDAO subtitlesDAO) {
        return subtitlesService.addSubtitles(subtitlesDAO);
    }

    @Override
    public ResponseDTO updateSubtitles(SubtitlesDAO subtitlesDAO) {
        return subtitlesService.updateSubtitles(subtitlesDAO);
    }

    @Override
    public ResponseDTO deleteSubtitles(Long id) {
        return subtitlesService.deleteSubtitles(id);
    }
}
