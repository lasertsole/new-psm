package com.psm.application;

import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.domain.User.entity.User.UserVO;
import com.psm.utils.DTO.ResponseDTO;
import com.psm.utils.OSS.UploadOSSUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@RestController
@RequestMapping("/users")
@ConfigurationProperties(prefix = "aliyun.oss.path.users")
public class UserController {
    @Autowired
    UserAdaptor userAdaptor;

    @Autowired
    UploadOSSUtil uploadOSSUtil;

    String avatarFolderPath;

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody UserDTO userDTO){
        try {
            // 登录
            Map<String, Object> map = userAdaptor.login(userDTO);

            return new ResponseDTO(HttpStatus.OK, "Login successful", map);
        }
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (LockedException e){
            return new ResponseDTO(HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
        }
        catch (BadCredentialsException e){
            return new ResponseDTO(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        catch (DisabledException e){
            return new ResponseDTO(HttpStatus.FORBIDDEN, e.getMessage());
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseDTO register(@RequestBody UserDTO userDTO){
        try {
            //注册
            Map<String, Object> map = userAdaptor.register(userDTO);

            return new ResponseDTO(HttpStatus.OK, "Login successful", map);
        }
        catch (DuplicateKeyException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/logout")
    public ResponseDTO logout()
    {
        try {
            userAdaptor.logout();
            return ResponseDTO.ok("Logout successful");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseDTO deleteUser() {
        try {
            userAdaptor.deleteUser();
            return ResponseDTO.ok("Delete user successful");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/updateUser")
    public ResponseDTO updateUser(@RequestBody UserDTO userDTO) {
        try {
            userAdaptor.updateUser(userDTO);
            return ResponseDTO.ok("Update user successful");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/updatePassword")
    public ResponseDTO updatePassword(@RequestBody UserDTO userDTO) {
        try {
            userAdaptor.updatePassword(userDTO);
            return ResponseDTO.ok("Update password successful");
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseDTO getUserByID(@PathVariable Long id)
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        try {
            // 获取用户信息
            UserVO userVO = userAdaptor.getUserByID(userDTO);

            // 封装数据
            Map<String, Object> map = new HashMap<>();
            map.put("user", userVO);

            return new ResponseDTO(HttpStatus.OK, "Get user successful", map);
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseDTO getUserByName(@RequestParam String name) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(name);

        try {
            // 获取用户信息
            List<UserVO> userVOList = userAdaptor.getUserByName(userDTO);
            Map<String, Object> map = new HashMap<>();
            map.put("users", userVOList);

            return new ResponseDTO(HttpStatus.OK, "Get users successful", map);
        }catch (IllegalArgumentException e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
    }
}
