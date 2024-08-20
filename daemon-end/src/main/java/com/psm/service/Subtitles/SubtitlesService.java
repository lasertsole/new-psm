package com.psm.service.Subtitles;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.Subtitles.SubtitlesDAO;
import com.psm.domain.UtilsDom.ResponseDTO;


/**
 * 橱窗领域服务
 *
 * @author moye
 * @date 2021/05/08
 */
public interface SubtitlesService extends IService<SubtitlesDAO> {
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
