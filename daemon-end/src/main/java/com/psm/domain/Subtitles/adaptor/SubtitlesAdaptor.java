package com.psm.domain.Subtitles.adaptor;

import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.domain.Subtitles.entity.SubtitlesVO;
import com.psm.types.entity.page.PageDTO;
import org.springframework.dao.DuplicateKeyException;

import java.security.InvalidParameterException;
import java.util.List;

public interface SubtitlesAdaptor {
    /**
     * 分页查询橱窗盒子
     *
     * @param pageDTO
     * @return List<SubtitlesVO>
     */
    public List<SubtitlesVO> getSubtitlesListByPage(PageDTO pageDTO) throws InvalidParameterException;

    /**
     * 根据id查询橱窗盒子
     *
     * @param subtitlesDTO
     * @return SubtitlesVO
     */
    public SubtitlesVO getSubtitlesById(SubtitlesDTO subtitlesDTO) throws InvalidParameterException;

    /**
     * 根据userId查询橱窗盒子
     *
     * @param subtitlesDTO
     * @return
     */
    List<SubtitlesVO> getSubtitlesByUserId(SubtitlesDTO subtitlesDTO);

    /**
     * 增加橱窗盒子
     *
     * @param subtitlesDTO
     * @return void
     */
    public void addSubtitles(SubtitlesDTO subtitlesDTO) throws DuplicateKeyException, InvalidParameterException;

    /**
     * 修改橱窗盒子
     *
     * @param subtitlesDTO
     * @return void
     */
    public void updateSubtitles(SubtitlesDTO subtitlesDTO) throws InvalidParameterException;

    /**
     * 删除橱窗盒子
     *
     * @param subtitlesDTO
     * @return void
     */
    public void deleteSubtitles(SubtitlesDTO subtitlesDTO) throws InvalidParameterException;
}
