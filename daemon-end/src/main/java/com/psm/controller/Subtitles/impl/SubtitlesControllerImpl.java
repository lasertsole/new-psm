package com.psm.controller.Subtitles.impl;

import com.psm.controller.Subtitles.SubtitlesController;
import com.psm.domain.UtilsDom.PageDTO;
import com.psm.domain.Subtitles.SubtitlesDAO;
import com.psm.domain.Subtitles.SubtitlesDTO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.Subtitles.SubtitlesManagerService;
import com.psm.utils.OSS.UploadOSSUtil;
import io.micrometer.common.util.StringUtils;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Setter
@RestController
@RequestMapping("/subtitles")
@ConfigurationProperties(prefix = "aliyun.oss.path.subtitles")
public class SubtitlesControllerImpl implements SubtitlesController {

    @Autowired
    SubtitlesManagerService subtitlesManagerService;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    String imageFolderPath;

    String videoFolderPath;

    @GetMapping("/{id}")
    public ResponseDTO getSubtitlesById(@PathVariable Long id) {
        return subtitlesManagerService.getSubtitlesById(id);
    }

    @GetMapping
    public ResponseDTO getSubtitlesList(@Valid @ModelAttribute PageDTO pageDTO) {
        return subtitlesManagerService.getSubtitlesListByPage(pageDTO.getCurrentPage(),pageDTO.getPageSize());
    }

    /**
     * 上传图片
     * （接收FormData类型）
     * @param subtitlesDTO
     * @return ResponseDTO
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseDTO addSubtitles(@Valid SubtitlesDTO subtitlesDTO) throws Exception {
        // 参数校验
        if(
                StringUtils.isBlank(subtitlesDTO.getTitle())||
                StringUtils.isBlank(subtitlesDTO.getContent())||
                StringUtils.isBlank(subtitlesDTO.getCategory())||
                Objects.isNull(subtitlesDTO.getCover())||
                Objects.isNull(subtitlesDTO.getVideo())
        ){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "The parameters cannot be empty");
        }

        String coverUrl;
        String videoUrl;

        try{
            coverUrl = uploadOSSUtil.multipartUpload(subtitlesDTO.getCover(),imageFolderPath);
        }
        catch (Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("error",e);
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Cover upload failed",map);
        }

        try{
            videoUrl = uploadOSSUtil.multipartUpload(subtitlesDTO.getCover(),videoFolderPath);
        }
        catch (Exception e){
            String coverOSSPath = new URL(coverUrl).getPath().substring(1);
            uploadOSSUtil.deleteFile(coverOSSPath);

            Map<String, Object> map = new HashMap<>();
            map.put("error",e);
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Video upload failed",map);
        }

        SubtitlesDAO subtitlesDAO = new SubtitlesDAO();
        BeanUtils.copyProperties(subtitlesDTO, subtitlesDAO);
        subtitlesDAO.setCover(coverUrl);
        subtitlesDAO.setVideo(videoUrl);
        System.out.println(subtitlesDAO);

        return subtitlesManagerService.addSubtitles(subtitlesDAO);
    }

    @PutMapping("/{id}")
    public ResponseDTO updateSubtitles(@PathVariable Long id, @Valid @RequestBody SubtitlesDTO subtitlesDTO) {
        SubtitlesDAO subtitlesDAO = new SubtitlesDAO();
        BeanUtils.copyProperties(subtitlesDTO, subtitlesDAO);
        return subtitlesManagerService.updateSubtitles(subtitlesDAO);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteSubtitles(@PathVariable Long id) {
        return subtitlesManagerService.deleteSubtitles(id);
    }
}