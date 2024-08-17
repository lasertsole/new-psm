package com.psm.controller;

import com.psm.domain.Page.PageDTO;
import com.psm.domain.Subtitles.SubtitlesDTO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.SubtitlesService;
import com.psm.utils.OSS.UploadOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/subtitles")
public class SubtitlesController {
    @Autowired
    SubtitlesService subtitlesService;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    @GetMapping("/{id}")
    public ResponseDTO getShowcaseById(@PathVariable Long id) {
        return subtitlesService.getShowcaseById(id);
    }

    @GetMapping
    public ResponseDTO getShowcaseList(@Valid @ModelAttribute PageDTO pageDTO) {
        return subtitlesService.getShowcaseListByPage(pageDTO.getCurrentPage(),pageDTO.getPageSize());
    }

    @GetMapping("/test")
    public ResponseDTO test() {
        return new ResponseDTO(HttpStatus.OK, "上传成功");
    }

    /**
     * 上传图片
     *
     * @param imageFile
     * @return ResponseDTO
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseDTO addShowcase(@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        //把图片上传到阿里云oss
        String url = uploadOSSUtil.upload(imageFile);
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return new ResponseDTO(HttpStatus.OK, "上传成功", map);
    }

    @PutMapping("/{id}")
    public ResponseDTO updateShowcase(@PathVariable Long id, @Valid @RequestBody SubtitlesDTO showcaseDTO) {
        return subtitlesService.updateShowcase(showcaseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteShowcase(@PathVariable Long id) {
        return subtitlesService.deleteShowcase(id);
    }
}