package com.psm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.DAO.UserDAO;
import com.psm.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MybatisPlusPluginTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testPage() {//分页测试
        Page<UserDAO> page = new Page<>(1,3);//当前第1页，每页3条数据
        Page<UserDAO> resultPage = userMapper.selectPage(page,null);

        // 获取分页结果
        List<UserDAO> records = resultPage.getRecords(); // 获取当前页的记录列表
        long total = resultPage.getTotal(); // 获取总记录数
        long currentPage = resultPage.getCurrent(); // 获取当前页码
        long pageSize = resultPage.getSize(); // 获取每页大小
        long pages = resultPage.getPages(); // 获取总页数

        // 输出分页结果
        System.out.println("Total records: " + total);
        System.out.println("Current page: " + currentPage);
        System.out.println("Page size: " + pageSize);
        System.out.println("Pages: " + pages);
        System.out.println("Records: " + records);
    }

    @Test
    public void textOptimisticLock(){//测试乐观锁
        UserDAO user = userMapper.selectById(1819366672368062466L);
        user.setName("yee");
        userMapper.updateById(user);
    }
}
