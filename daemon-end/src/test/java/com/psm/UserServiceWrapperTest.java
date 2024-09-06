package com.psm;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.repository.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class UserServiceWrapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01() {//测试封装查询条件
        QueryWrapper<UserDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("phone", "201");
        List<UserDAO> list =  userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test02(){//测试封装排序条件
        QueryWrapper<UserDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc( "phone").orderByDesc("email");
        List<UserDAO> list =  userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test03(){//测试封装删除条件
        QueryWrapper<UserDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email","");
        int result = userMapper.delete(queryWrapper);
        System.out.println("result = " + result);
    }

    @Test
    public void test04(){//测试封装更新条件
        QueryWrapper<UserDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("phone",202).or().lt("phone",277);
        UserDAO user = new UserDAO();
        user.setPhone("222");
        int result = userMapper.update(user,queryWrapper);
        System.out.println("result = " + result);
    }

    @Test
    public void test05(){//测试封装条件优先级
        QueryWrapper<UserDAO> queryWrapper = new QueryWrapper<>();
        //lambda表达式中的条件优先执行
        queryWrapper.like("name","ybc").and(i->i.gt("phone",110).or().eq("email",""));
        List<UserDAO> list =  userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test06(){//测试查询有限字段
        QueryWrapper<UserDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","name","phone");
        List<Map<String, Object>> maps= userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
    }

    @Test
    public void test07(){//测试子查询
        QueryWrapper<UserDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id","select id from tb_user where phone > '111'");
        List<UserDAO> list =  userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test08(){//测试updateWrapper,同test04
        UpdateWrapper<UserDAO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("phone","201").set("sex",false);
        int result = userMapper.update(null,updateWrapper);
        System.out.println("result = " + result);
    }

    @Test
    public void test09(){//测试condition查询
        String name = "a";
        String phone = "111";
        QueryWrapper<UserDAO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(name!=null,"name",name).eq(phone!=null,"phone",phone);
        List<UserDAO> list =  userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test10(){//LambdaQueryWrapper写法
        String name = "a";
        String phone = "111";
        LambdaQueryWrapper<UserDAO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name!=null, UserDAO::getName,name).eq(phone!=null, UserDAO::getPhone,phone);
        List<UserDAO> list =  userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

    @Test
    public void test11(){//LambdaUpdateWrapper写法
        String name = "a";
        String phone = "111";
        LambdaUpdateWrapper<UserDAO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.like(UserDAO::getName,"ybc").and(i->i.gt(UserDAO::getPhone,110).or().eq(UserDAO::getEmail,""));
        int result = userMapper.update(null,updateWrapper);
        System.out.println("result = " + result);
    }

}
