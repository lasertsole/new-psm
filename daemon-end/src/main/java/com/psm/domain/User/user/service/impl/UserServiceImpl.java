package com.psm.domain.User.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.User.user.entity.LoginUser.LoginUser;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.domain.User.user.repository.LoginUserRedis;
import com.psm.domain.User.user.repository.UserOSS;
import com.psm.domain.User.user.repository.UserDB;
import com.psm.domain.User.user.service.UserService;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import com.psm.domain.User.user.types.enums.SexEnum;
import com.psm.domain.User.user.Event.bus.security.utils.JWT.JWTUtil;
import com.psm.types.enums.VisibleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDB userDB;

    @Autowired
    private UserOSS userOSS;

    @Autowired
    private LoginUserRedis loginUserRedis;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${spring.security.jwt.expiration}")
    private Long expiration;//jwt有效期

    /**
     * 登录多级缓存
     */
    private final Cache loginCache;

    public UserServiceImpl(CacheManager cacheManager) {
        this.loginCache = cacheManager.getCache("loginCache");
    }

    @Override
    public UserBO getAuthorizedUser(){
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        return UserConvertor.INSTANCE.DO2BO(loginUser.getUserDO());
    }

    @Override
    public Long getAuthorizedUserId() {
        return getAuthorizedUser().getId();
    }

    @Override
    public UserBO login(String name, String password) throws LockedException,BadCredentialsException,DisabledException{
        // AuthenticationManager authenticate进行认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(name, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 如果认证通过了，使用id生成jwt
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        UserDO loginUserInfo = loginUser.getUserDO();
        String id = loginUserInfo.getId().toString();
        String jwt = jwtUtil.createJWT(id);


        // 把完整信息存入redis，id作为key(如果原先有则覆盖)
        loginCache.put("login:"+id, loginUser);

        UserBO userBO = new UserBO();
        BeanUtils.copyProperties(loginUserInfo, userBO);
        userBO.setToken(jwt);

        return userBO;
    }

    @Override
    public void logout() {
        // 获取SecurityContextHolder中的用户id
        Long id = getAuthorizedUserId();

        // 根据用户id删除redis中的用户信息
        loginCache.evict("login:"+id);
    }

    @Override
    @Transactional
    public UserBO register(String name, String password, String email) throws DuplicateKeyException{
        // 将前端传来的user对象拷贝到register对象中,并加密register对象的密码
        UserDO register = new UserDO();
        register.setName(name);
        register.setPassword(password);
        register.setEmail(email);
        register.setPassword(passwordEncoder.encode(register.getPassword()));

        // 将register对象保存到数据库
        userDB.save(register);

        // 使用未加密密码的user对象登录
        return login(name, password);
    }

    @Override
    public void deleteUser() {
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUserDO().getId();

        // 根据用户id删除redis中的用户信息
        loginUserRedis.removeLoginUser(String.valueOf(id));

        // 根据用户id删除pg中的用户信息
        userDB.removeById(id);
    }

    @Override
    public String updateAvatar(String oldAvatar, MultipartFile newAvatarFile) throws Exception {
        // 获取SecurityContextHolder中的用户id
        String userId = String.valueOf(getAuthorizedUserId());

        // 更新oss中用户头像信息
        String avatarUrl = userOSS.updateAvatar(oldAvatar, newAvatarFile, userId);

        // 更新数据库中用户头像信息
        Long id = getAuthorizedUserId();//获取SecurityContextHolder中的用户id
        UserDO userDO = new UserDO();
        userDO.setId(id);
        userDO.setAvatar(avatarUrl);
        userDB.updateAvatar(userDO);

        // 更新redis中用户头像信息
        loginUserRedis.updateLoginUser(userDO);

        return avatarUrl;
    }

    @Override
    public void updateInfo(String name, Boolean sex, String phone, String email, String profile) {
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUserDO().getId();

        // 将UserDTO转化为UserDO
        UserDO userDO = new UserDO();
        if(Objects.nonNull(name)) userDO.setName(name);
        if(Objects.nonNull(sex)) userDO.setSex(sex ? SexEnum.FEMALE : SexEnum.MALE);
        if(Objects.nonNull(phone)) userDO.setPhone(phone);
        if(Objects.nonNull(email)) userDO.setEmail(email);
        if(Objects.nonNull(profile)) userDO.setProfile(profile);
        userDO.setId(id);

        // 更新用户信息
        userDB.updateInfo(userDO);

        // 更新redis中用户信息
        loginUserRedis.updateLoginUser(userDO);
    }

    @Override
    public void updatePassword(String password, String changePassword) {
        // 获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUserDO().getId();

        // 判断新密码是否与旧密码相同
        if(password.equals(changePassword)){
            throw new RuntimeException("New password cannot be the same as the old password");
        }

        // 获取数据库中用户的password
        UserDO userDO = new UserDO();
        userDO.setId(id);
        String passwordFromDB = userDB.findPasswordById(userDO);

        // 判断旧密码是否正确
        if(!passwordEncoder.matches(password,passwordFromDB)){
            throw new RuntimeException("Password error");
        }

        // 将新密码加密
        String encodePassword = passwordEncoder.encode(changePassword);

        // 将新密码覆盖数据库中的password
        userDO.setId(id);
        userDO.setPassword(encodePassword);
        userDB.updatePasswordById(userDO);

        // 更新redis中用户信息
        loginUserRedis.updateLoginUser(userDO);
    }

    @Override
    public UserBO getUserByID(Long id) {
        // 获取用户信息
        UserDO userDO = userDB.getById(id);

        // 判断用户是否存在
        if(Objects.isNull(userDO)){
            return UserConvertor.INSTANCE.DO2BO(userDO);
        }
        else{// 用户不存在
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public List<UserBO> getUserByName(String name) {
        // 获取用户信息
        UserDO userDO = new UserDO();
        userDO.setName(name);
        List<UserDO> userDOs = userDB.findUsersByName(userDO);

        // 判断用户是否存在
        if(!userDOs.isEmpty()){
            return userDOs.stream().map(UserConvertor.INSTANCE::DO2BO).collect(Collectors.toList());
        }
        else{// 用户不存在
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public List<UserBO> getUserOrderByCreateTimeAsc(Integer currentPage, Integer pageSize){
        // 分页
        Page<UserDO> page = new Page<>(currentPage,pageSize);

        // 返回结果
        return userDB.selectUserOrderByCreateTimeAsc(page).stream().map(UserConvertor.INSTANCE::DO2BO).collect(Collectors.toList());
    }

    @Override
    public List<UserBO> getUserByIds(List<Long> ids) {
        return userDB.selectUserByIds(ids).stream().map(UserConvertor.INSTANCE::DO2BO).collect(Collectors.toList());
    }

    @Override
    public boolean updateOnePublicModelNumById(Long id, short work_num) {
        UserDO userDO = new UserDO();
        userDO.setId(id);
        userDO.setPublicModelNum(work_num);

        return userDB.updateById(userDO);
    }

    @Override
    public boolean addOnePublicModelNumById(Long id) {
        UserDO userDO = userDB.selectById(id);
        short work_num = userDO.getPublicModelNum();

        return updateOnePublicModelNumById(id, (short) (work_num + 1));
    }

    @Override
    public boolean removeOnePublicModelNumById(Long id) {
        UserDO userDO = userDB.selectById(id);
        short work_num = userDO.getPublicModelNum();

        if ( work_num == 0) return false;

        if ( work_num < 0) return updateOnePublicModelNumById(id, (short) 0);

        return updateOnePublicModelNumById(id, (short) (work_num - 1));
    }

    @Override
    public Long updateOnePublicModelStorageById(Long id, Long storage) {
        UserDO userDO = new UserDO();
        userDO.setId(id);
        userDO.setModelCurStorage(storage);
        if(!userDB.updateById(userDO)) throw new RuntimeException("The user does not exist.");

        return storage;
    }

    @Override
    public Long addOnePublicModelStorageById(Long id, Long storage) {
        UserDO userDO = userDB.selectById(id);
        long modelCurStorage = userDO.getModelCurStorage();
        long modelMaxStorage = userDO.getModelMaxStorage();
        long newStorage = modelCurStorage + storage;
        if (newStorage > modelMaxStorage) throw new RuntimeException("The storage exceeds the maximum limit.");

        return updateOnePublicModelStorageById(id, modelCurStorage + storage);
    }

    @Override
    public Long minusOnePublicModelStorageById(Long id, Long storage) {
        UserDO userDO = userDB.selectById(id);
        long modelCurStorage = userDO.getModelCurStorage();
        long newStorage = modelCurStorage - storage;
        if (newStorage < 0) newStorage = 0;

        return updateOnePublicModelStorageById(id, newStorage);
    }

    @Async("asyncThreadPoolExecutor")// 使用有界异步线程池处理该方法
    public void processUploadModel3D(Long userId, Long modelSize, VisibleEnum visible) {
            // 如果模型设置为公开，更新数据库中用户上传公开模型数量+1
            if (Objects.equals(visible, VisibleEnum.PUBLIC)) {
                addOnePublicModelNumById(userId);
            }

            // 增加用户已用的存储空间为当前文件大小
            addOnePublicModelStorageById(userId, modelSize);
    }
}