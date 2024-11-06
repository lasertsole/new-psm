package com.psm.ModelShowBar;

import com.psm.domain.Model.modelsUserBind.repository.impl.ModelsUserBindDBImpl;
import com.psm.trigger.http.Model.ModelController;
import com.psm.types.utils.page.PageDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ModelShowBarTest {
    @Autowired
    private ModelsUserBindDBImpl modelsUserBindDBImpl;

    @Test
    public void getModelsShowBars() {
        log.info("selectModelsShowBars is {}", modelsUserBindDBImpl.selectModelsShowBars(
                1,10, null, null, null, null).getRecords());
    }
}
