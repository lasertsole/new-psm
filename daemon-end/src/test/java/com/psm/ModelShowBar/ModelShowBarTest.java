package com.psm.ModelShowBar;

import com.psm.trigger.mvc.Model.ModelController;
import com.psm.types.utils.page.PageDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ModelShowBarTest {
    @Autowired
    private ModelController modelController;

    @Test
    void test(){
        PageDTO pageDTO = new PageDTO(1, 10);
        log.info("modelsShowBarAdaptor is {}", modelController.getModelsShowBars(pageDTO));
    }
}
