//package com.psm.domain.Subtitles.adaptor.impl;
//
//import com.psm.app.annotation.spring.Adaptor;
//import com.psm.domain.Subtitles.adaptor.SubtitlesAdaptor;
//import com.psm.domain.Subtitles.entity.SubtitlesDO;
//import com.psm.domain.Subtitles.entity.SubtitlesDTO;
//import com.psm.domain.Subtitles.entity.SubtitlesVO;
//import com.psm.domain.Subtitles.types.convertor.SubtitlesConvertor;
//import com.psm.domain.Subtitles.service.SubtitlesService;
//import com.psm.utils.page.PageDTO;
//import io.micrometer.common.util.StringUtils;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.security.InvalidParameterException;
//import java.util.List;
//import java.util.Objects;
//
//@Slf4j
//@Adaptor
//public class SubtitlesAdaptorImpl implements SubtitlesAdaptor {
//    @Autowired
//    SubtitlesService subtitlesService;
//
//    @Override
//    public List<SubtitlesVO> getSubtitlesListByPage(@Valid PageDTO pageDTO) throws InvalidParameterException{
//        // 获取字幕盒子
//        List<SubtitlesDO> subtitlesDOList = subtitlesService.getSubtitlesListByPage(
//                pageDTO.getCurrent(),
//                pageDTO.getSize());
//        // 判断字幕盒子是否存在
//        if(Objects.isNull(subtitlesDOList)){
//            throw new RuntimeException("The Subtitles does not exist.");
//        }
//
//        // 将DO转换为VO
//        return subtitlesDOList.stream().map(
//                subtitlesDO -> {
//                    return SubtitlesConvertor.INSTANCE.DO2VO(subtitlesDO);
//                }
//        ).toList();
//    };
//
//    @Override
//    public SubtitlesVO getSubtitlesById(@Valid SubtitlesDTO subtitlesDTO) throws InvalidParameterException{
//        // 参数判空
//        if(Objects.isNull(subtitlesDTO.getId()))
//            throw new InvalidParameterException("Invalid parameter");
//
//        // 获取字幕盒子
//        SubtitlesDO subtitlesDO = subtitlesService.getSubtitlesById(subtitlesDTO.getId());
//
//        // 判断字幕盒子是否存在
//        if(subtitlesDO == null){
//            throw new RuntimeException("The Subtitles does not exist.");
//        }
//
//        // 将DO转换为VO
//        return SubtitlesConvertor.INSTANCE.DO2VO(subtitlesDO);
//    };
//
//    public List<SubtitlesVO> getSubtitlesByUserId(@Valid SubtitlesDTO subtitlesDTO){
//        // 参数判空
//        if(Objects.isNull(subtitlesDTO.getUserId()))
//            throw new InvalidParameterException("Invalid parameter");
//
//        // 获取字幕盒子列表
//        List<SubtitlesDO> subtitlesDOList = subtitlesService.getSubtitlesByUserId(subtitlesDTO.getUserId());
//
//        // 判断字幕盒子是否存在
//        if(subtitlesDOList == null){
//            throw new RuntimeException("The Subtitles does not exist.");
//        }
//
//        // 将DO转换为VO
//        return subtitlesDOList.stream().map(subtitlesDO -> {
//            return SubtitlesConvertor.INSTANCE.DO2VO(subtitlesDO);
//        }).toList();
//    };
//
//    @Override
//    public void addSubtitles(@Valid SubtitlesDTO subtitlesDTO) throws DuplicateKeyException, InvalidParameterException{
//        String title = subtitlesDTO.getTitle();
//        String content = subtitlesDTO.getContent();
//        MultipartFile cover = subtitlesDTO.getCover();
//        MultipartFile video = subtitlesDTO.getVideo();
//        String style = subtitlesDTO.getStyle();
//        String type = subtitlesDTO.getType();
//
//        // 参数判空
//        if (
//                StringUtils.isBlank(title)
//                || StringUtils.isBlank(content)
//                || Objects.isNull(cover)
//                || Objects.isNull(video)
//                || StringUtils.isBlank(style)
//                || StringUtils.isBlank(type)
//        )
//            throw new InvalidParameterException("Invalid parameter");
//
//        // 添加字幕盒子
//        subtitlesService.addSubtitles(title, content, cover, video, style, type);
//    };
//
//    @Override
//    public void updateSubtitles(@Valid SubtitlesDTO subtitlesDTO) throws InvalidParameterException{
//        String title = subtitlesDTO.getTitle();
//        String content = subtitlesDTO.getContent();
//        MultipartFile cover = subtitlesDTO.getCover();
//        MultipartFile video = subtitlesDTO.getVideo();
//        String style = subtitlesDTO.getStyle();
//        String type = subtitlesDTO.getType();
//
//        // 参数判空
//        if (
//                StringUtils.isBlank(title)
//                        || StringUtils.isBlank(content)
//                        || Objects.isNull(cover)
//                        || Objects.isNull(video)
//                        || StringUtils.isBlank(style)
//                        || StringUtils.isBlank(type)
//        )
//            throw new InvalidParameterException("Invalid parameter");
//
//        // 更新字幕盒子
//        subtitlesService.updateSubtitles(title, content, cover, video, style, type);
//    };
//
//    @Override
//    public void deleteSubtitles(@Valid SubtitlesDTO subtitlesDTO) throws InvalidParameterException{
//        // 参数判空
//        if (Objects.isNull(subtitlesDTO.getId()))
//            throw new InvalidParameterException("Invalid parameter");
//
//        // 删除字幕盒子
//        subtitlesService.deleteSubtitles(subtitlesDTO.getId());
//    };
//}
