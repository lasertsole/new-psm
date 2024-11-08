package com.psm.ModelShowBar;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.models_user.entity.ModelUserDAO;
import com.psm.domain.Model.models_user.repository.impl.Models_UserDBImpl;
import com.psm.domain.Model.models_user.valueObject.Models_UserDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.infrastructure.DB.ModelMapper;
import com.psm.infrastructure.DB.UserMapper;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@Slf4j
@SpringBootTest
public class ModelShowBarTest {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    Models_UserDBImpl models_userDB;

//    @Test
//    public void getModelsShowBars() {
//        List<ModelUserDAO> modelUserDAOS = modelMapper.selectModelsShowBars(
//                1, 10, false, false, "architecture", "retro", 338126150792515584L);
//
//        Map<Long, Models_UserDAO> userMap = new HashMap<>();
//
//        log.info("modelUserDAOS is {}", modelUserDAOS);
//
//        modelUserDAOS.forEach(
//            modelUserDAO -> {
//                ModelDAO model = new ModelDAO(
//                    modelUserDAO.getModelId(),
//                    null,
//                    modelUserDAO.getTitle(),
//                    null,
//                        modelUserDAO.getCover(),
//                    null,
//                    null,
//                    null,
//                    modelUserDAO.getStyle(),
//                    modelUserDAO.getType(),
//                    modelUserDAO.getCreateTime(),
//                    null,
//                    null,
//                    null
//                );
//
//                if (!userMap.containsKey(modelUserDAO.getUserId())){
//                    UserDAO user = new UserDAO(
//                        modelUserDAO.getUserId(),
//                        modelUserDAO.getName(),
//                        null,
//                        null,
//                        modelUserDAO.getAvatar(),
//                        null,
//                        modelUserDAO.getSex(),
//                        modelUserDAO.getProfile(),
//                        modelUserDAO.getPublicModelNum(),
//                        null,
//                        null,
//                        modelUserDAO.getIsIdle(),
//                        modelUserDAO.getCanUrgent(),
//                        null,
//                        null,
//                        null,
//                        null
//                    );
//
//                    List<ModelDAO> models = new ArrayList<>();
//                    models.add(model);
//                    Models_UserDAO models_UserDAO = new Models_UserDAO(user, models);
//                    userMap.put(modelUserDAO.getUserId(), models_UserDAO);
//                }
//                else{
//                    userMap.get(modelUserDAO.getUserId()).getModels().add(model);
//                }
//            }
//        );
//
//        List<Models_UserDAO> values = new ArrayList<>(userMap.values());
//        Page<Models_UserDAO> page = new Page<>();
//        page.setRecords(values);
//        log.info("page.getRecords() is {}", page.getRecords());
//        log.info("page.getPages() is {}", page.getPages());
//        log.info("page.getCurrent() is {}", page.getCurrent());
//        log.info("page.getSize() is {}", page.getSize());
//        log.info("page.getTotal() is {}", page.getTotal());
//    }

    @Test
    public void getModelsShowBars() {
        Page<Models_UserDAO> page = models_userDB.selectModelsShowBars(
                1, 10, false, false, "architecture", "retro", 338126150792515584L);

        log.info("page.getRecords() is {}", page.getRecords());
        log.info("page.getPages() is {}", page.getPages());
        log.info("page.getCurrent() is {}", page.getCurrent());
        log.info("page.getSize() is {}", page.getSize());
        log.info("page.getTotal() is {}", page.getTotal());
    }
}
