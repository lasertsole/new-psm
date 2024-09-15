package com.psm.application;

import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.domain.User.entity.User.UserVO;
import com.psm.infrastructure.utils.DTO.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

@Setter
@RestController
@RequestMapping("/users")
@ConfigurationProperties(prefix = "aliyun.oss.path.users")
public class UserController {
    @Autowired
    UserAdaptor userAdaptor;

    // 前端地址
    @Value("${server.front-end-url.socket}")
    private String frontEndBaseUrl;

    // 前端登录页面
    @Value("${server.front-end-url.login-page}")
    private String loginPage;

    // 返回前端登录视图
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect(frontEndBaseUrl+loginPage);
    };

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody UserDTO userDTO, HttpServletResponse response){
        try {
            // 登录
            Map<String, Object> map = userAdaptor.login(userDTO);
            response.setHeader("token", (String) map.get("token"));
            return new ResponseDTO(HttpStatus.OK, "Login successful", map.get("user"));
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

    // 快速登录
    @GetMapping("/fastLogin")
    public ResponseDTO fastLogin() {
        try {
            UserVO userVO = userAdaptor.getAuthorizedUser();
            return ResponseDTO.ok("FastLogin successful", userVO);
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseDTO register(@RequestBody UserDTO userDTO, HttpServletResponse response){
        try {
            //注册
            Map<String, Object> map = userAdaptor.register(userDTO);
            response.setHeader("token", (String) map.get("token"));
            return new ResponseDTO(HttpStatus.OK, "Login successful", map.get("user"));
        }
        catch (DuplicateKeyException | InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/logout")
    public ResponseDTO logout() {
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

    @PutMapping("/updateAvatar")
    public ResponseDTO updateAvatar(UserDTO userDTO) {
        try {
            String avatarUrl = userAdaptor.updateAvatar(userDTO);
            return ResponseDTO.ok("Update avatar successful", avatarUrl);
        }
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
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
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
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
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseDTO getUserByID(@PathVariable Long id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        try {
            // 获取用户信息
            UserVO userVO = userAdaptor.getUserByID(userDTO);

            return new ResponseDTO(HttpStatus.OK, "Get user successful", userVO);
        }
        catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
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

            return new ResponseDTO(HttpStatus.OK, "Get users successful", userVOList);
        }catch (IllegalArgumentException e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
    }
}
