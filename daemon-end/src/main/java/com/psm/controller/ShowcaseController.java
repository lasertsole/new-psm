package com.psm.controller;

import com.psm.domain.Page.PageDTO;
import com.psm.domain.Showcase.ShowcaseDTO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.ShowcaseService;
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
@RequestMapping("/showcases")
public class ShowcaseController {
    @Autowired
    ShowcaseService showcaseService;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        return null;
    }

    @GetMapping("/{id}")
    public Object getShowcaseById(@PathVariable Long id) {
        return showcaseService.getShowcaseById(id);
    }

    @GetMapping
    public Object getShowcaseList(@Valid @ModelAttribute PageDTO pageDTO) {
        return showcaseService.getShowcaseListByPage(pageDTO.getCurrentPage(),pageDTO.getPageSize());
    }

    @PostMapping
    public ResponseDTO addShowcase(MultipartFile imageFile) throws IOException {
        //把图片上传到阿里云oss
        String url = uploadOSSUtil.upload(imageFile);
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return new ResponseDTO(HttpStatus.OK, "上传成功", map);
    }

    @PutMapping("/{id}")
    public Object updateShowcase(@PathVariable Long id, @Valid @RequestBody ShowcaseDTO showcaseDTO) {
        return showcaseService.updateShowcase(showcaseDTO);
    }

    @DeleteMapping("/{id}")
    public Object deleteShowcase(@PathVariable Long id) {
        return showcaseService.deleteShowcase(id);
    }
}