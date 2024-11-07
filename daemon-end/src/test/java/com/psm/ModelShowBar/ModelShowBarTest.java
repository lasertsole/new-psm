package com.psm.ModelShowBar;

import com.psm.domain.Model.models_user.repository.impl.Models_UserDBImpl;
import com.psm.infrastructure.DB.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ModelShowBarTest {
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void getModelsShowBars() {
        log.info("selectModelsShowBars is {}", modelMapper.selectModelsShowBars(
                1,10, false, false, "architecture", "retro", 338126150792515584L));
    }
}
