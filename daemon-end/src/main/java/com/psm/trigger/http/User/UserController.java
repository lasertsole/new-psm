package com.psm.trigger.http.User;

import com.psm.domain.Independent.User.Single.user.adaptor.UserAdaptor;
import com.psm.domain.Independent.User.Single.user.entity.User.UserBO;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDTO;
import com.psm.types.common.DTO.ResponseDTO;
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

import java.security.InvalidParameterException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserAdaptor userAdaptor;

    // 前端地址
    @Value("${server.front-end-url.socket}")
    private String frontEndBaseUrl;

    // 前端登录页面
    @Value("${server.front-end-url.login-page}")
    private String loginPage;

//    // 返回前端登录视图
//    @GetMapping("/login")
//    public void login(HttpServletResponse response) throws IOException {
//        response.sendRedirect(frontEndBaseUrl+loginPage);
//    };

    /**
     * 用户登录
     *
     * @param userDTO 用户DTO对象，包括用户名、密码
     * @param response HttpServletResponse对象，用于设置token
     * @return ResponseVO
     */
    @PostMapping("/login")
    public ResponseDTO login(@RequestBody UserDTO userDTO, HttpServletResponse response){
        try {
            UserBO userBO = UserBO.fromDTO(userDTO);
            // 登录
            userBO = userAdaptor.login(userBO);
            response.setHeader("token", userBO.getToken());

            return new ResponseDTO(HttpStatus.OK, "Login successful", userBO.toCurrentDTO());
        } catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        } catch (LockedException e){
            return new ResponseDTO(HttpStatus.TOO_MANY_REQUESTS, "TOO_MANY_REQUESTS");
        } catch (BadCredentialsException e){
            return new ResponseDTO(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        } catch (DisabledException e){
            return new ResponseDTO(HttpStatus.FORBIDDEN, "SERVER FORBIDDEN");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 快速登录，用于前端自动登录
     *
     * @return ResponseVO
     */
    @GetMapping("/fastLogin")
    public ResponseDTO fastLogin() {
        try {
            UserBO userBO = userAdaptor.getAuthorizedUser();

            return ResponseDTO.ok("FastLogin successful", userBO.toCurrentDTO());
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 用户注册
     *
     * @param userDTO 用户DTO对象，包括用户名、密码、邮箱、手机号等
     * @param response HttpServletResponse对象，用于设置token
     * @return ResponseVO
     */
    @PostMapping("/register")
    public ResponseDTO register(@RequestBody UserDTO userDTO, HttpServletResponse response){
        try {
            UserBO userBO = UserBO.fromDTO(userDTO);
            //注册
            userBO = userAdaptor.register(userBO);
            response.setHeader("token", userBO.getToken());

            return new ResponseDTO(HttpStatus.OK, "register successful", userBO.toCurrentDTO());
        } catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        } catch (DuplicateKeyException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "DuplicateKey");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 用户下线
     *
     * @return ResponseVO
     */
    @DeleteMapping("/logout")
    public ResponseDTO logout() {
        try {
            userAdaptor.logout();
            return ResponseDTO.ok("Logout successful");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 用户注销
     *
     * @return ResponseVO
     */
    @DeleteMapping("/deleteUser")
    public ResponseDTO deleteUser() {
        try {
            userAdaptor.deleteUser();
            return ResponseDTO.ok("Delete user successful");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 更新用户头像
     *
     * @param userDTO 用户DTO对象，包括用户ID、头像URL等
     * @return ResponseVO
     */
    @PutMapping("/updateAvatar")
    public ResponseDTO updateAvatar(UserDTO userDTO) {
        try {
            UserBO userBO = UserBO.fromDTO(userDTO);
            String avatarUrl = userAdaptor.updateAvatar(userBO);
            return ResponseDTO.ok("Update avatar successful", avatarUrl);
        } catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 更新用户信息
     *
     * @param userDTO 用户DTO对象，包括用户ID、用户名、邮箱、手机号等
     * @return ResponseVO
     */
    @PutMapping("/updateInfo")
    public ResponseDTO updateUser(@RequestBody UserDTO userDTO) {
        try {
            UserBO userBO = UserBO.fromDTO(userDTO);
            userAdaptor.updateInfo(userBO);
            return ResponseDTO.ok("Update user successful");
        } catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 更新用户密码
     *
     * @param userDTO 用户DTO对象，包括用户ID、旧密码、新密码等
     * @return ResponseVO
     */
    @PutMapping("/updatePassword")
    public ResponseDTO updatePassword(@RequestBody UserDTO userDTO) {
        try {
            UserBO userBO = UserBO.fromDTO(userDTO);
            userAdaptor.updatePassword(userBO);
            return ResponseDTO.ok("Update password successful");
        } catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return ResponseVO
     */
    @GetMapping("/{id}")
    public ResponseDTO getUserByID(@PathVariable String id) {
        try {
            // 构造业务User对象
            UserBO userBO = new UserBO();
            userBO.setId(Long.valueOf(id));

            // 获取用户信息
            userBO = userAdaptor.getUserById(userBO);

            return new ResponseDTO(HttpStatus.OK, "Get user successful", userBO);
        } catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userIds 用户ID列表
     * @return ResponseVO
     */
    @GetMapping
    public ResponseDTO getUserByIds(@RequestParam List<String> userIds) {
        try {
            // 将用户ID列表转换为UserDTO列表
            List<Long> ids = userIds.stream().map(Long::valueOf).toList();

            // 调用服务层方法获取用户信息列表
            List<UserBO> userBOList = userAdaptor.getUserByIds(ids);

            return new ResponseDTO(HttpStatus.OK, "Get users successful", userBOList);
        } catch (InvalidParameterException e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST, "InvalidParameter");
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }
}
