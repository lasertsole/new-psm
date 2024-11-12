package com.psm.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.domain.User.follower.repository.FollowerDB;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TestClass {
    @Autowired
    private FollowerDB followerDB;

    @Test
    public void test() {
        FollowerDAO followerDAO = new FollowerDAO();
        followerDAO.setTgtUserId(345618849405734912L);
        followerDAO.setSrcUserId(345615872179703808L);
        LambdaQueryWrapper<FollowerDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowerDAO::getTgtUserId,followerDAO.getTgtUserId())
                .and(w-> w.eq(FollowerDAO::getSrcUserId,followerDAO.getSrcUserId()));

        boolean b = followerDB.saveOrUpdate(followerDAO, wrapper);;
        log.info("b: {}", b);
    }
}
