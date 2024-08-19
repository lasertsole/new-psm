package com.psm.controller;

import com.psm.domain.Page.PageDTO;
import com.psm.domain.Subtitles.SubtitlesDAO;
import com.psm.domain.Subtitles.SubtitlesDTO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.SubtitlesService;
import com.psm.utils.OSS.UploadOSSUtil;
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

@Setter
@RestController
@RequestMapping("/subtitles")
@ConfigurationProperties(prefix = "aliyun.oss.path.subtitles")
public class SubtitlesController {

    @Autowired
    SubtitlesService subtitlesService;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    String imageFolderPath;

    String videoFolderPath;

    @GetMapping("/{id}")
    public ResponseDTO getSubtitlesById(@PathVariable Long id) {
        return subtitlesService.getSubtitlesById(id);
    }

    @GetMapping
    public ResponseDTO getSubtitlesList(@Valid @ModelAttribute PageDTO pageDTO) {
        return subtitlesService.getSubtitlesListByPage(pageDTO.getCurrentPage(),pageDTO.getPageSize());
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

        return subtitlesService.addSubtitles(subtitlesDAO);
    }

    @PutMapping("/{id}")
    public ResponseDTO updateSubtitles(@PathVariable Long id, @Valid @RequestBody SubtitlesDTO subtitlesDTO) {
        SubtitlesDAO subtitlesDAO = new SubtitlesDAO();
        BeanUtils.copyProperties(subtitlesDTO, subtitlesDAO);
        return subtitlesService.updateSubtitles(subtitlesDAO);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteSubtitles(@PathVariable Long id) {
        return subtitlesService.deleteSubtitles(id);
    }
}