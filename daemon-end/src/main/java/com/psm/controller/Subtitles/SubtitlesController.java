package com.psm.controller.Subtitles;

import com.psm.domain.UtilsDom.PageDTO;
import com.psm.domain.Subtitles.SubtitlesDTO;
import com.psm.domain.UtilsDom.ResponseDTO;

public interface SubtitlesController {

    /**
     * 根据id获取字幕盒子
     *
     * @param id
     * @return
     */
    public ResponseDTO getSubtitlesById(Long id);

    /**
     * 获取所有字幕盒子
     *
     * @param pageDTO
     * @return
     */
    public ResponseDTO getSubtitlesList(PageDTO pageDTO);

    /**
     * 添加字幕盒子
     *
     * @param subtitlesDTO
     * @return
     */
    public ResponseDTO addSubtitles(SubtitlesDTO subtitlesDTO) throws Exception;

    /**
     * 删除字幕盒子
     *
     * @param id
     * @return
     */
    public ResponseDTO updateSubtitles(Long id,SubtitlesDTO subtitlesDTO);

    /**
     * 更新字幕盒子
     *
     * @param id
     * @return
     */
    public ResponseDTO deleteSubtitles(Long id);
}
