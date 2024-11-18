//package com.psm.domain.Subtitles.service;
//
//import com.psm.domain.Subtitles.entity.SubtitlesDO;
//import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepository;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//
///**
// * 橱窗领域服务
// *
// * @author moye
// * @date 2021/05/08
// */
//public interface SubtitlesService extends BaseDBRepository<SubtitlesDO> {
//    /**
//     * 分页查询橱窗盒子
//     *
//     * @param currentPage 当前页码
//     * @param pageSize 页大小
//     * @return 字幕DO列表
//     */
//    List<SubtitlesDO> getSubtitlesListByPage(Integer currentPage, Integer pageSize);
//
//    /**
//     * 根据id查询橱窗盒子
//     *
//     * @param id 字幕id
//     * @return 字幕DO
//     */
//    SubtitlesDO getSubtitlesById(Long id);
//
//    /**
//     * 根据userId查询橱窗盒子
//     *
//     * @param userId 用户id
//     * @return 字幕DO列表
//     */
//    List<SubtitlesDO> getSubtitlesByUserId(Long userId);
//
//    /**
//     * 增加橱窗盒子
//     *
//     * @param title 标题
//     * @param content 内容
//     * @param cover 封面
//     * @param video 视频
//     * @param style 样式
//     * @param type 类型
//     */
//    public void addSubtitles(String title, String content, MultipartFile cover, MultipartFile video, String style, String type) throws DuplicateKeyException;
//
//    /**
//     * 修改橱窗盒子
//     *
//     * @param title 标题
//     * @param content 内容
//     * @param cover 封面
//     * @param video 视频
//     * @param style 样式
//     * @param type 类型
//     */
//    public void updateSubtitles(String title, String content, MultipartFile cover, MultipartFile video, String style, String type);
//
//    /**
//     * 删除橱窗盒子
//     *
//     * @param id
//     * @return
//     */
//    public void deleteSubtitles(Long id);
//}
