package com.psm.application;

import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDTO;
import com.psm.infrastructure.utils.VO.ResponseVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@RestController
@RequestMapping("/users")
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
    public ResponseVO login(@RequestBody UserDTO userDTO, HttpServletResponse response){
        try {
            // 登录
            Map<String, Object> map = userAdaptor.login(userDTO);
            response.setHeader("token", (String) map.get("token"));
            UserBO userBO = (UserBO) map.get("user");
            return new ResponseVO(HttpStatus.OK, "Login successful", userBO.toCurrentUserVO());
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        }
        catch (LockedException e){
            return new ResponseVO(HttpStatus.TOO_MANY_REQUESTS, "TOO_MANY_REQUESTS");
        }
        catch (BadCredentialsException e){
            return new ResponseVO(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }
        catch (DisabledException e){
            return new ResponseVO(HttpStatus.FORBIDDEN, "SERVER FORBIDDEN");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    // 快速登录
    @GetMapping("/fastLogin")
    public ResponseVO fastLogin() {
        try {
            UserBO userBO = userAdaptor.getAuthorizedUser();
            return ResponseVO.ok("FastLogin successful", userBO.toCurrentUserVO());
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    @PostMapping("/register")
    public ResponseVO register(@RequestBody UserDTO userDTO, HttpServletResponse response){
        try {
            //注册
            Map<String, Object> map = userAdaptor.register(userDTO);
            response.setHeader("token", (String) map.get("token"));
            UserBO userBO = (UserBO) map.get("user");
            return new ResponseVO(HttpStatus.OK, "Login successful", userBO.toCurrentUserVO());
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        }
        catch (DuplicateKeyException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, "DuplicateKey");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    @DeleteMapping("/logout")
    public ResponseVO logout() {
        try {
            userAdaptor.logout();
            return ResponseVO.ok("Logout successful");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseVO deleteUser() {
        try {
            userAdaptor.deleteUser();
            return ResponseVO.ok("Delete user successful");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    @PutMapping("/updateAvatar")
    public ResponseVO updateAvatar(UserDTO userDTO) {
        try {
            String avatarUrl = userAdaptor.updateAvatar(userDTO);
            return ResponseVO.ok("Update avatar successful", avatarUrl);
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    @PutMapping("/updateInfo")
    public ResponseVO updateUser(@RequestBody UserDTO userDTO) {
        try {
            userAdaptor.updateInfo(userDTO);
            return ResponseVO.ok("Update user successful");
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    @PutMapping("/updatePassword")
    public ResponseVO updatePassword(@RequestBody UserDTO userDTO) {
        try {
            userAdaptor.updatePassword(userDTO);
            return ResponseVO.ok("Update password successful");
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    @GetMapping("/{id}")
    public ResponseVO getUserByID(@PathVariable Long id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        try {
            // 获取用户信息
            UserBO userBO = userAdaptor.getUserById(userDTO);

            return new ResponseVO(HttpStatus.OK, "Get user successful", userBO);
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    @GetMapping
    public ResponseVO getUserByName(@RequestParam String name) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(name);

        try {
            // 获取用户信息
            List<UserBO> userBOs = userAdaptor.getUserByName(userDTO);

            return new ResponseVO(HttpStatus.OK, "Get users successful", userBOs);
        }
        catch (IllegalArgumentException e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "The parameters cannot be empty");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }
}
