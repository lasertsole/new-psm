package com.psm.service.Showcase;

import com.psm.domain.Showcase.ShowcaseDAO;
import com.psm.service.ShowcaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ShowcaseTest {
    @Autowired
    private ShowcaseService showcaseService;

    @Test
    public void getShowcaseList() {
        List<ShowcaseDAO> showcaseList = showcaseService.getShowcaseListByPage(1,20);
        System.out.println(showcaseList);
        ShowcaseDAO showcase = showcaseList.get(0);
        System.out.println(showcase.getCategory());
    }
}
