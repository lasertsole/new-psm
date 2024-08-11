package com.psm.controller;

import com.psm.domain.Page.PageDTO;
import com.psm.domain.Showcase.ShowcaseDTO;
import com.psm.service.ShowcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/showcases")
public class ShowcaseController {
    @Autowired
    ShowcaseService showcaseService;

    @GetMapping("/cata")
    public Object showcase(){return null;};

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