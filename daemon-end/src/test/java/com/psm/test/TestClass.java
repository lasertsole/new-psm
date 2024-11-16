package com.psm.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.domain.User.relationships.entity.RelationshipsDAO;
import com.psm.domain.User.relationships.repository.RelationshipsDB;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TestClass {
    @Autowired
    private RelationshipsDB followerDB;

    @Test
    public void test() {
        RelationshipsDAO followerDAO = new RelationshipsDAO();
        followerDAO.setTgtUserId(345618849405734912L);
        followerDAO.setSrcUserId(345615872179703808L);
        LambdaQueryWrapper<RelationshipsDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationshipsDAO::getTgtUserId,followerDAO.getTgtUserId())
                .and(w-> w.eq(RelationshipsDAO::getSrcUserId,followerDAO.getSrcUserId()));

        boolean b = followerDB.saveOrUpdate(followerDAO, wrapper);;
        log.info("b: {}", b);
    }
}
