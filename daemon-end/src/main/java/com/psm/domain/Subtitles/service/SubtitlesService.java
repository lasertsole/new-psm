package com.psm.domain.Subtitles.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.Subtitles.entity.SubtitlesDAO;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;


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
    public List<SubtitlesDAO> getSubtitlesListByPage(Integer currentPage, Integer pageSize);

    /**
     * 根据id查询橱窗盒子
     *
     * @param id
     * @return
     */
    public SubtitlesDAO getSubtitlesById(Long id);

    /**
     * 根据userId查询橱窗盒子
     *
     * @param userId
     * @return
     */
    List<SubtitlesDAO> getSubtitlesByUserId(Long userId);

    /**
     * 增加橱窗盒子
     *
     * @param subtitlesDTO
     * @return
     */
    public void addSubtitles(SubtitlesDTO subtitlesDTO) throws DuplicateKeyException;

    /**
     * 修改橱窗盒子
     *
     * @param subtitlesDTO
     * @return
     */
    public void updateSubtitles(SubtitlesDTO subtitlesDTO);

    /**
     * 删除橱窗盒子
     *
     * @param id
     * @return
     */
    public void deleteSubtitles(Long id);
}
