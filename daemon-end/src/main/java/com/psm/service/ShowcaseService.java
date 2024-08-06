package com.psm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.Showcase.ShowcaseDAO;
import com.psm.domain.Showcase.ShowcaseDTO;
import com.psm.domain.UtilsDom.ResponseDTO;

import java.util.List;


/**
 * 橱窗服务
 *
 * @author moye
 * @date 2021/05/08
 */
public interface ShowcaseService extends IService<ShowcaseDAO> {
    /**
     * 分页查询橱窗盒子
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    public ResponseDTO getShowcaseListByPage(Integer currentPage, Integer pageSize);

    /**
     * 根据id查询橱窗盒子
     *
     * @param id
     * @return
     */
    public ResponseDTO getShowcaseById(Long id);

    /**
     * 增加橱窗盒子
     *
     * @param showcaseDTO
     * @return
     */
    public ResponseDTO addShowcase(ShowcaseDTO showcaseDTO);

    /**
     * 修改橱窗盒子
     *
     * @param showcaseDTO
     * @return
     */
    public ResponseDTO updateShowcase(ShowcaseDTO showcaseDTO);

    /**
     * 删除橱窗盒子
     *
     * @param id
     * @return
     */
    public ResponseDTO deleteShowcase(Long id);
}
