package com.psm.controller;

import com.psm.domain.Page.PageDTO;
import com.psm.domain.Subtitles.SubtitlesDTO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.SubtitlesService;
import com.psm.utils.OSS.UploadOSSUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        return subtitlesService.getShowcaseById(id);
    }

    @GetMapping
    public ResponseDTO getSubtitlesList(@Valid @ModelAttribute PageDTO pageDTO) {
        return subtitlesService.getShowcaseListByPage(pageDTO.getCurrentPage(),pageDTO.getPageSize());
    }

    /**
     * 上传图片
     *
     * @param cover
     * @return ResponseDTO
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseDTO addSubtitles(@RequestParam("cover") MultipartFile cover) throws Exception {
        //把图片上传到阿里云oss
        String url = uploadOSSUtil.upload(cover,imageFolderPath);
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        uploadOSSUtil.multipartUpload(cover,imageFolderPath);
        return new ResponseDTO(HttpStatus.OK, "上传成功", map);
    }

    @PutMapping("/{id}")
    public ResponseDTO updateSubtitles(@PathVariable Long id, @Valid @RequestBody SubtitlesDTO showcaseDTO) {
        return subtitlesService.updateShowcase(showcaseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteSubtitles(@PathVariable Long id) {
        return subtitlesService.deleteShowcase(id);
    }
}