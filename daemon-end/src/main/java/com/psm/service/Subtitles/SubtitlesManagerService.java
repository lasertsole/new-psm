package com.psm.service.Subtitles;

import com.psm.domain.Subtitles.SubtitlesDAO;
import com.psm.domain.UtilsDom.ResponseDTO;

/**
 * 橱窗应用服务
 *
 * @author moye
 * @date 2024/08/21
 */
public interface SubtitlesManagerService {
    /**
     * 分页查询橱窗盒子
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    public ResponseDTO getSubtitlesListByPage(Integer currentPage, Integer pageSize);

    /**
     * 根据id查询橱窗盒子
     *
     * @param id
     * @return
     */
    public ResponseDTO getSubtitlesById(Long id);

    /**
     * 增加橱窗盒子
     *
     * @param subtitlesDAO
     * @return
     */
    public ResponseDTO addSubtitles(SubtitlesDAO subtitlesDAO);

    /**
     * 修改橱窗盒子
     *
     * @param subtitlesDAO
     * @return
     */
    public ResponseDTO updateSubtitles(SubtitlesDAO subtitlesDAO);

    /**
     * 删除橱窗盒子
     *
     * @param id
     * @return
     */
    public ResponseDTO deleteSubtitles(Long id);
}
