package com.psm.domain.Subtitles.adaptor.impl;

import com.psm.infrastructure.annotation.spring.Adaptor;
import com.psm.domain.Subtitles.adaptor.SubtitlesAdaptor;
import com.psm.domain.Subtitles.entity.SubtitlesDAO;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.domain.Subtitles.entity.SubtitlesVO;
import com.psm.domain.Subtitles.infrastructure.convertor.SubtitlesInfrastructure;
import com.psm.domain.Subtitles.service.SubtitlesService;
import com.psm.infrastructure.utils.MybatisPlus.Page.PageDTO;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Adaptor
public class SubtitlesAdaptorImpl implements SubtitlesAdaptor {
    @Autowired
    SubtitlesService subtitlesService;

    @Override
    public List<SubtitlesVO> getSubtitlesListByPage(@Valid PageDTO pageDTO) throws InvalidParameterException{
        // 获取字幕盒子
        List<SubtitlesDAO> subtitlesDAOList = subtitlesService.getSubtitlesListByPage(
                pageDTO.getCurrent(),
                pageDTO.getPage());
        // 判断字幕盒子是否存在
        if(subtitlesDAOList == null){
            throw new RuntimeException("The Subtitles does not exist.");
        }

        // 将DAO转换为VO
        return subtitlesDAOList.stream().map(
                subtitlesDAO -> {
                    return SubtitlesInfrastructure.DAOConvertToVO(subtitlesDAO);
                }
        ).toList();
    };

    @Override
    public SubtitlesVO getSubtitlesById(@Valid SubtitlesDTO subtitlesDTO) throws InvalidParameterException{
        // 参数判空
        if(Objects.isNull(subtitlesDTO.getId())){
            throw new InvalidParameterException("Invalid parameter");
        }

        // 获取字幕盒子
        SubtitlesDAO subtitlesDAO = subtitlesService.getSubtitlesById(subtitlesDTO.getId());

        // 判断字幕盒子是否存在
        if(subtitlesDAO == null){
            throw new RuntimeException("The Subtitles does not exist.");
        }

        // 将DAO转换为VO
        return SubtitlesInfrastructure.DAOConvertToVO(subtitlesDAO);
    };

    public List<SubtitlesVO> getSubtitlesByUserId(@Valid SubtitlesDTO subtitlesDTO){
        // 参数判空
        if(Objects.isNull(subtitlesDTO.getUserId())){
            throw new InvalidParameterException("Invalid parameter");
        }

        // 获取字幕盒子列表
        List<SubtitlesDAO> subtitlesDAOList = subtitlesService.getSubtitlesByUserId(subtitlesDTO.getUserId());

        // 判断字幕盒子是否存在
        if(subtitlesDAOList == null){
            throw new RuntimeException("The Subtitles does not exist.");
        }

        // 将DAO转换为VO
        return subtitlesDAOList.stream().map(subtitlesDAO -> {
            return SubtitlesInfrastructure.DAOConvertToVO(subtitlesDAO);
        }).toList();
    };

    @Override
    public void addSubtitles(@Valid SubtitlesDTO subtitlesDTO) throws DuplicateKeyException, InvalidParameterException{
        log.info("subtitlesDTO is {}", subtitlesDTO);
        // 参数判空
        if (
                StringUtils.isBlank(subtitlesDTO.getTitle()) ||
                StringUtils.isBlank(subtitlesDTO.getContent()) ||
                Objects.isNull(subtitlesDTO.getCover()) ||
                Objects.isNull(subtitlesDTO.getVideo()) ||
                StringUtils.isBlank(subtitlesDTO.getCategory())
        )
        {
            throw new InvalidParameterException("Invalid parameter");
        }

        // 添加字幕盒子
        subtitlesService.addSubtitles(subtitlesDTO);
    };

    @Override
    public void updateSubtitles(@Valid SubtitlesDTO subtitlesDTO) throws InvalidParameterException{
        // 参数判空
        if (
                StringUtils.isBlank(subtitlesDTO.getTitle()) &&
                StringUtils.isBlank(subtitlesDTO.getContent()) &&
                Objects.isNull(subtitlesDTO.getCover()) &&
                Objects.isNull(subtitlesDTO.getVideo()) &&
                StringUtils.isBlank(subtitlesDTO.getCategory())
        )
        {
            throw new InvalidParameterException("Invalid parameter");
        }

        // 更新字幕盒子
        subtitlesService.updateSubtitles(subtitlesDTO);
    };

    @Override
    public void deleteSubtitles(@Valid SubtitlesDTO subtitlesDTO) throws InvalidParameterException{
        // 参数判空
        if (Objects.isNull(subtitlesDTO.getId()))
        {
            throw new InvalidParameterException("Invalid parameter");
        }

        // 删除字幕盒子
        subtitlesService.deleteSubtitles(subtitlesDTO.getId());
    };
}
