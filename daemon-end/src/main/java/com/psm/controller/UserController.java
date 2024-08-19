package com.psm.controller;

import com.psm.domain.User.UserDAO;
import com.psm.domain.User.UserDTO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.UserService;
import com.psm.utils.OSS.UploadOSSUtil;
import io.micrometer.common.util.StringUtils;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Setter
@RestController
@RequestMapping("/users")
@ConfigurationProperties(prefix = "aliyun.oss.path.users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    String avatarFolderPath;

    @PostMapping("/login")//登录
    public ResponseDTO login(@Valid @RequestBody UserDTO userDto){
        // 参数校验
        if(
                StringUtils.isBlank(userDto.getName())||
                StringUtils.isBlank(userDto.getPassword())
        ){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "The parameters cannot be empty");
        }

        UserDAO user = new UserDAO();
        BeanUtils.copyProperties(userDto, user);
        return userService.login(user);
    }

    @PostMapping("/register")//注册
    public ResponseDTO register(@Valid @RequestBody UserDTO userDto){
        // 参数校验
        if(
                StringUtils.isBlank(userDto.getName())||
                StringUtils.isBlank(userDto.getPassword())
        ){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "The parameters cannot be empty");
        }

        //注册
        UserDAO user = new UserDAO();
        BeanUtils.copyProperties(userDto, user);
        return userService.register(user);
    }

    @DeleteMapping("/logout")//登出
    public ResponseDTO logout()
    {
        return userService.logout();
    }

    @DeleteMapping("/deleteUser")//销号
    public ResponseDTO deleteUser() {
        return userService.deleteUser();
    }

    @PutMapping("/updateUser")//更新用户信息(除了密码)
    public ResponseDTO updateUser(@Valid @RequestBody UserDTO userDto) {
        String avatarUrl = null;
        if (!Objects.isNull(userDto.getAvatar())){
            try{
                avatarUrl = uploadOSSUtil.multipartUpload(userDto.getAvatar(),avatarFolderPath);
            }
            catch (Exception e){
                Map<String, Object> map = new HashMap<>();
                map.put("error",e);
                return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "avatarUrl upload failed",map);
            }
        }

        UserDAO userDao = new UserDAO();
        BeanUtils.copyProperties(userDto, userDao);
        userDao.setAvatar(avatarUrl);
        return userService.updateUser(userDao);
    }

    @PutMapping("/updatePassword")//更新密码
    public ResponseDTO updatePassword(@Valid @RequestBody UserDTO userDto) {
        String password = userDto.getPassword();
        String changePassword = userDto.getChangePassword();
        return userService.updatePassword(password,changePassword);
    }

    @GetMapping("/{id}")//通过ID获取用户信息
    public ResponseDTO getUserByID(@PathVariable Long id) {
        return null;
    }

    @GetMapping//通过用户名获取用户信息
    public ResponseDTO getUserByName(@RequestParam("username") String name) {
        return null;
    }
}
