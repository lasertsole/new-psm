package com.psm.application;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;
import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.domain.User.entity.User.UserVO;
import com.psm.utils.DTO.ResponseDTO;
import com.psm.utils.OSS.UploadOSSUtil;
import com.psm.utils.ThirdAuth.github.GithubAuthProperties;
import com.psm.utils.ThirdAuth.github.GithubAuthUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @Autowired
    GithubAuthUtil githubAuthUtil;

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

    @GetMapping("/githubLogin")
    public void githubLogin(HttpServletResponse response) throws IOException {
        //获取Github认证服务的属性信息
        GithubAuthProperties githubAuthProperties = githubAuthUtil.getGithubAuthProperties();

        // 生成并保存state，忽略该参数有可能导致CSRF攻击
        String state = githubAuthUtil.genState();
        // 传递参数response_type、client_id、state、redirect_uri
        String param = "response_type=code&" + "client_id=" + githubAuthProperties.getClientId() + "&state=" + state
                + "&redirect_uri=" + githubAuthProperties.getDirectUrl();

        // 请求Github认证服务器
        response.sendRedirect(githubAuthProperties.getAuthorizeUrl() + "?" + param);
    }

    @GetMapping("/githubCallback")
    public String githubCallback(String code, String state, HttpServletResponse response) throws Exception {
        // 验证state，如果不一致，可能被CSRF攻击
        if(!githubAuthUtil.checkState(state)) {
            throw new Exception("State验证失败");
        }

        //获取Github认证服务的属性信息
        GithubAuthProperties githubAuthProperties = githubAuthUtil.getGithubAuthProperties();

        // 设置JSONObject请求体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("client_id",githubAuthProperties.getClientId());
        jsonObject.put("client_secret",githubAuthProperties.getClientSecret());
        jsonObject.put("code",code);

        String accessTokenRequestJson = null;
        try{
            long start = System.currentTimeMillis();
            // 请求accessToken，成功获取到后进行下一步信息获取,这里第一次可能会超时
            accessTokenRequestJson = HttpRequest.post(githubAuthProperties.getAccessTokenUrl())
                    .header("Accept"," application/json")
                    .body(jsonObject.toJSONString())
                    .timeout(30000)
                    .execute().body();
        }catch (Exception e){
            throw new Exception(e);
        }

        JSONObject accessTokenObject = JSONObject.parseObject(accessTokenRequestJson);
        // 如果返回的数据包含error，表示失败，错误原因存储在error_description
        if(accessTokenObject.containsKey("error")) {
            throw  new Exception("error_description，令牌获取错误");
        }
        // 如果返回结果中包含access_token，表示成功
        if(!accessTokenObject.containsKey("access_token")) {
            throw  new Exception("获取token失败");
        }

        // 得到token和token_type
        String accessToken = (String) accessTokenObject.get("access_token");
        String tokenType = (String) accessTokenObject.get("token_type");
        String userInfo = null;
        try{
            long start = System.currentTimeMillis();
            // 请求资源服务器获取个人信息
            userInfo = HttpRequest.get(githubAuthProperties.getResourceUrl())
                    .header("Authorization", tokenType + " " + accessToken)
                    .timeout(5000)
                    .execute().body();
        }catch (Exception e){
            throw new Exception(e);
        }

        JSONObject userInfoJson = JSONObject.parseObject(userInfo);
        return userInfoJson.toJSONString();
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
    public ResponseDTO getUserByID(@PathVariable Long id) {
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
