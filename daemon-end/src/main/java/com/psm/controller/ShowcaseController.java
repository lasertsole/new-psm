package com.psm.controller;

import com.psm.domain.Page.PageDTO;
import com.psm.domain.Showcase.ShowcaseDTO;
import com.psm.service.ShowcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/showcases")
public class ShowcaseController {
    @Autowired
    ShowcaseService showcaseService;


    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 指定文件保存路径
            String filePath = "/var/www/uploads/" + fileName;
            // 将文件写入磁盘
            File destFile = new File(filePath);
            file.transferTo(destFile);
            return "File uploaded successfully: " + fileName;
        } catch (IOException e) {
            return "Error uploading file: " + e.getMessage();
        }
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
    public Object addShowcase(@Valid @RequestBody ShowcaseDTO showcaseDTO) {
        return null;
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