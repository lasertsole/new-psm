package com.psm.controller.User.impl;

import com.psm.controller.User.UserController;
import com.psm.domain.User.UserDAO;
import com.psm.domain.User.UserDTO;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.service.User.UserManagerService;
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
public class UserControllerImpl implements UserController {
    @Autowired
    UserManagerService userManagerService;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    String avatarFolderPath;

    @PostMapping("/login")
    public ResponseDTO login(@Valid @RequestBody UserDTO userDto){
        // 参数校验
        if(
                StringUtils.isBlank(userDto.getName())||
                StringUtils.isBlank(userDto.getPassword())
        ){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "The parameters cannot be empty");
        }

        UserDAO userDAO = new UserDAO();
        BeanUtils.copyProperties(userDto, userDAO);
        return userManagerService.login(userDAO);
    }

    @PostMapping("/register")
    public ResponseDTO register(@Valid @RequestBody UserDTO userDto){
        // 参数校验
        if(
                StringUtils.isBlank(userDto.getName())||
                StringUtils.isBlank(userDto.getPassword())
        ){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "The parameters cannot be empty");
        }

        //注册
        UserDAO userDAO = new UserDAO();
        BeanUtils.copyProperties(userDto, userDAO);
        return userManagerService.register(userDAO);
    }

    @DeleteMapping("/logout")
    public ResponseDTO logout()
    {
        return userManagerService.logout();
    }

    @DeleteMapping("/deleteUser")
    public ResponseDTO deleteUser() {
        return userManagerService.deleteUser();
    }

    @PutMapping("/updateUser")
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
        return userManagerService.updateUser(userDao);
    }

    @PutMapping("/updatePassword")
    public ResponseDTO updatePassword(@Valid @RequestBody UserDTO userDto) {
        String password = userDto.getPassword();
        String changePassword = userDto.getChangePassword();
        return userManagerService.updatePassword(password,changePassword);
    }

    @GetMapping("/{id}")
    public ResponseDTO getUserByID(@PathVariable Long id)
    {
        return null;
    }

    @GetMapping
    public ResponseDTO getUserByName(@RequestParam String name) {
        try {
            // 参数校验
            if(StringUtils.isBlank(name)){
                return new ResponseDTO(HttpStatus.BAD_REQUEST, "The parameters cannot be empty");
            }
            UserDTO userDto = new UserDTO();
            userDto.setName(name);
            userDto.validateField("name");

            return null;
        }catch (IllegalArgumentException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "The parameters cannot be empty");
        }
    }
}
