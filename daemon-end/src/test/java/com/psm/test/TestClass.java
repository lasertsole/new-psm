package com.psm.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.domain.Model.model.adaptor.Model3dAdaptor;
import com.psm.domain.User.relationships.entity.RelationshipsDO;
import com.psm.domain.User.relationships.repository.RelationshipsDB;
import com.psm.trigger.http.Model.ModelController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TestClass {
    @Autowired
    private ModelController modelController;

    @Test
    public void test() {
        log.info("test is {}", modelController.getModelsShowBars(1, 10, null, null, null, null, null));
    }
}
