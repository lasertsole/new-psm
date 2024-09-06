package com.psm.application;

import com.psm.domain.Subtitles.adaptor.SubtitlesAdaptor;
import com.psm.domain.Subtitles.entity.SubtitlesVO;
import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.infrastructure.utils.DTO.PageDTO;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.infrastructure.utils.DTO.ResponseDTO;
import com.psm.infrastructure.utils.OSS.UploadOSSUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Setter
@RestController
@RequestMapping("/subtitles")
@ConfigurationProperties(prefix = "aliyun.oss.path.subtitles")
public class SubtitlesController {

    @Autowired
    SubtitlesAdaptor subtitlesAdaptor;

    @Autowired
    UserAdaptor userAdaptor;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    String imageFolderPath;

    String videoFolderPath;

    @GetMapping("/{id}")
    public ResponseDTO getSubtitlesById(@PathVariable Long userId) {
        try {
            // 获取视频信息
            SubtitlesDTO subtitlesDTO = new SubtitlesDTO();
            subtitlesDTO.setUserId(userId);
            SubtitlesVO subtitlesVO = subtitlesAdaptor.getSubtitlesById(subtitlesDTO);

            // 返回数据
            return new ResponseDTO(HttpStatus.OK, "Get subtitles successful", subtitlesVO);
        }
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseDTO getSubtitlesList(@ModelAttribute PageDTO pageDTO) {
        try {
            // 获取视频列表
            List<SubtitlesVO> subtitlesVOList = subtitlesAdaptor.getSubtitlesListByPage(pageDTO);

            // 返回数据
            return new ResponseDTO(HttpStatus.OK, "Get subtitles successful", subtitlesVOList);
        }
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 上传图片
     * （接收FormData类型）
     * @param subtitlesDTO
     * @return ResponseDTO
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseDTO addSubtitles(SubtitlesDTO subtitlesDTO) throws Exception {
        try {
            subtitlesAdaptor.addSubtitles(subtitlesDTO);
            return new ResponseDTO(HttpStatus.OK, "Upload subtitles successful");
        }
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseDTO updateSubtitles(@PathVariable Long id, @RequestBody SubtitlesDTO subtitlesDTO) {
        try {
            //获取当前用户ID
            Long currentUserID = userAdaptor.getAuthorizedUserId();

            // 判断当前用户是否为视频上传者
            SubtitlesVO subtitlesVO = (SubtitlesVO) getSubtitlesById(id).getData();// 获取视频信息
            if (!Objects.equals(subtitlesVO.getUserId(), currentUserID)) {
                throw new InvalidParameterException("You are not the owner of this subtitles");
            }

            // 更新视频信息
            subtitlesAdaptor.updateSubtitles(subtitlesDTO);

            // 更新视频信息
            return new ResponseDTO(HttpStatus.OK, "Update subtitles successful");
        }
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteSubtitles(@PathVariable Long id) {
        try {
            //获取当前用户ID
            Long currentUserID = userAdaptor.getAuthorizedUserId();

            // 判断当前用户是否为视频上传者
            SubtitlesVO subtitlesVO = (SubtitlesVO) getSubtitlesById(id).getData();// 获取视频信息
            if (!Objects.equals(subtitlesVO.getUserId(), currentUserID)) {
                throw new InvalidParameterException("You are not the owner of this subtitles");
            }

            // 删除视频
            SubtitlesDTO subtitlesDTO = new SubtitlesDTO();
            subtitlesDTO.setId(id);
            subtitlesAdaptor.deleteSubtitles(subtitlesDTO);

            // 返回数据
            return new ResponseDTO(HttpStatus.OK, "Delete subtitles successful");
        }
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}