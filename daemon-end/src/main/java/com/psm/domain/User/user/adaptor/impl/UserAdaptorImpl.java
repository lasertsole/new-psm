package com.psm.domain.User.user.adaptor.impl;

import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.service.AuthUserService;
import com.psm.domain.User.user.service.UserService;
import com.psm.utils.Valid.ValidUtil;
import com.psm.domain.User.user.types.enums.SexEnum;
import com.psm.utils.page.PageBO;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.multipart.MultipartFile;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Adaptor
public class UserAdaptorImpl implements UserAdaptor {
    @Autowired
    ValidUtil validUtil;

    @Autowired
    UserService userService;

    @Autowired
    AuthUserService authUserService;

    @Override
    public String authUserToken(String token) {
        return authUserService.authUserToken(token);
    }

    @Override
    public UserBO getAuthorizedUser() {
        return userService.getAuthorizedUser();
    }

    @Override
    public Long getAuthorizedUserId() {
        return userService.getAuthorizedUserId();
    }

    @Override
    public UserBO login(UserBO userBO) throws LockedException, BadCredentialsException, DisabledException, InvalidParameterException, InstantiationException, IllegalAccessException {
        String name = StringEscapeUtils.escapeHtml4(userBO.getName());
        String password = userBO.getPassword();

        // 参数判空
        if(
                StringUtils.isBlank(name)
                ||StringUtils.isBlank(password)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("name", name), UserBO.class);

        // 登录
        userBO = userService.login(name, password);

        // 判断用户是否存在
        if(Objects.isNull(userBO.getToken())){
            throw new RuntimeException("The user does not exist.");
        }

        return userBO;
    }

    @Override
    public void logout() {
        userService.logout();
    }

    @Override
    public UserBO register(UserBO userBO) throws DuplicateKeyException, InvalidParameterException, InstantiationException, IllegalAccessException {
        String name = StringEscapeUtils.escapeHtml4(userBO.getName());
        String password = userBO.getPassword();
        String email = StringEscapeUtils.escapeHtml4(userBO.getEmail());

        // 参数判空
        if(
                StringUtils.isBlank(name)
                ||StringUtils.isBlank(password)
                ||StringUtils.isBlank(email)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("name", name, "email", email), UserBO.class);

        // 注册
        userBO = userService.register(name, password, email);

        // 判断用户是否存在
        if(Objects.isNull(userBO.getToken())){
            throw new RuntimeException("The user does not exist.");
        }

        return userBO;
    }

    @Override
    public void deleteUser() {
        userService.deleteUser();
    }

    @Override
    public String updateAvatar(@Valid UserBO userBO) throws InvalidParameterException, Exception{
        String oldAvatar = userBO.getOldAvatar();
        MultipartFile newAvatarFile = userBO.getAvatarFile();

        if (
                StringUtils.isBlank(oldAvatar)
                &&Objects.isNull(newAvatarFile)
        )
            throw new InvalidParameterException("Invalid parameter");

        return userService.updateAvatar(oldAvatar, newAvatarFile);
    };

    @Override
    public void updateInfo(UserBO userBO) throws InvalidParameterException, InstantiationException, IllegalAccessException {
        String name = StringEscapeUtils.escapeHtml4(userBO.getName());
        Boolean sex = Optional.ofNullable(userBO.getSex()).map(SexEnum::getValue).orElse(null);
        String phone = userBO.getPhone();
        String email = StringEscapeUtils.escapeHtml4(userBO.getEmail());
        String profile = StringEscapeUtils.escapeHtml4(userBO.getProfile());

        // 参数判空
        if(
                StringUtils.isBlank(name)
                &&Objects.isNull(sex)
                &&StringUtils.isBlank(phone)
                &&StringUtils.isBlank(email)
                &&StringUtils.isBlank(profile)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("name", name, "phone", phone, "email", email, "profile", profile), UserBO.class);

        // 修改用户
        userService.updateInfo(name, sex, phone, email, profile);
    }

    @Override
    public void updatePassword(@Valid UserBO userBO) throws InvalidParameterException {
        // 参数判空
        if(
                StringUtils.isBlank(userBO.getPassword())
                ||StringUtils.isBlank(userBO.getChangePassword())
        )
            throw new InvalidParameterException("Invalid parameter");

        // 修改密码
        userService.updatePassword(userBO.getPassword(), userBO.getChangePassword());
    }

    @Override
    public UserBO getUserById(@Valid UserBO userBO) throws InvalidParameterException {
        // 参数判空
        if(Objects.isNull(userBO.getId())){
            throw new InvalidParameterException("Invalid parameter");
        }

        // 获取用户
        Long id = userBO.getId();
        userBO = userService.getUserByID(id);

        // 判断用户是否存在
        if(Objects.isNull(userBO)){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDO转换为UserBO
        return userBO;
    }

    @Override
    public UserBO getUserById(Long id) throws InvalidParameterException, InstantiationException, IllegalAccessException {
        validUtil.validate(Map.of("id", id), UserBO.class);
        UserBO userBO = userService.getUserByID(id);

        // 判断用户是否存在
        if(Objects.isNull(userBO)){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDO转换为UserBO
        return userBO;
    }

    @Override
    public List<UserBO> getUserByName(UserBO userBO) throws InvalidParameterException, InstantiationException, IllegalAccessException {
        // 参数判空
        String name = StringEscapeUtils.escapeHtml4(userBO.getName());
        if(StringUtils.isBlank(userBO.getName()))
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("name", name), UserBO.class);

        List<UserBO> userBOList = userService.getUserByName(name);

        // 判断用户是否存在
        if(Objects.isNull(userBOList)){
            throw new RuntimeException("The user does not exist.");
        }

        return userBOList;
    }

    @Override
    public List<UserBO> getUserByName(String name) throws InvalidParameterException, InstantiationException, IllegalAccessException {
        name = StringEscapeUtils.escapeHtml4(name);
        validUtil.validate(Map.of("name", name), UserBO.class);

        List<UserBO> UserBOList = userService.getUserByName(name);

        // 判断用户是否存在
        if(Objects.isNull(UserBOList)){
            throw new RuntimeException("The user does not exist.");
        }

        // 将UserDO转换为UserBO
        return UserBOList;
    }

    @Override
    public List<UserBO> getUserOrderByCreateTimeAsc(@Valid PageBO pageBO){
        List<UserBO> userBOList = userService.getUserOrderByCreateTimeAsc(
                pageBO.getCurrent(),
                pageBO.getSize());

        // 判断用户列表是否存在
        if(userBOList == null){
            throw new RuntimeException("The Subtitles does not exist.");
        }

        // 将DO转换为BO
        return userBOList;
    }

    @Override
    public List<UserBO> getUserByIds(List<Long> ids) {
        return userService.getUserByIds(ids);
    }

    @Override
    public boolean updateOnePublicModelNumById(@Valid UserBO userBO) {
        if (
                Objects.isNull(userBO.getId())
                || Objects.isNull(userBO.getPublicModelNum())
        )
            throw new InvalidParameterException("Invalid parameter");

        return userService.updateOnePublicModelNumById(userBO.getId(), userBO.getPublicModelNum());
    }

    @Override
    public boolean addOnePublicModelNumById(Long id) throws InstantiationException, IllegalAccessException {
        if (Objects.isNull(id))
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", id), UserBO.class);

        return userService.addOnePublicModelNumById(id);
    }

    @Override
    public boolean addOnePublicModelNumById(@Valid UserBO userBO) throws InstantiationException, IllegalAccessException {
        if (Objects.isNull(userBO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        return userService.addOnePublicModelNumById(userBO.getId());
    }

    @Override
    public boolean removeOnePublicModelNumById(Long id) throws InstantiationException, IllegalAccessException {
        if (Objects.isNull(id))
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", id), UserBO.class);

        return userService.removeOnePublicModelNumById(id);
    }

    @Override
    public boolean removeOnePublicModelNumById(@Valid UserBO userBO) throws InstantiationException, IllegalAccessException {
        if (Objects.isNull(userBO.getId()))
            throw new InvalidParameterException("Invalid parameter");

        return userService.removeOnePublicModelNumById(userBO.getId());
    }

    @Override
    public Long updateOnePublicModelStorageById(Long id, Long storage) throws InstantiationException, IllegalAccessException {
        if (
                Objects.isNull(id)
                || Objects.isNull(storage)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", id, "storage", storage), UserBO.class);

        return userService.updateOnePublicModelStorageById(id, storage);
    }

    @Override
    public Long updateOnePublicModelStorageById(@Valid UserBO userBO) throws InstantiationException, IllegalAccessException {
        Long Id = userBO.getId();
        Long curStorage = userBO.getModelCurStorage();
        if (
                Objects.isNull(Id)
                || Objects.isNull(curStorage)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", Id, "storage", curStorage), UserBO.class);

        return userService.updateOnePublicModelStorageById(Id, curStorage);
    }

    @Override
    public Long addOnePublicModelStorageById(Long Id, Long storage) throws InstantiationException, IllegalAccessException {
        if (
                Objects.isNull(Id)
                || Objects.isNull(storage)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", Id, "storage", storage), UserBO.class);

        return userService.addOnePublicModelStorageById(Id, storage);
    }

    @Override
    public Long addOnePublicModelStorageById(@Valid UserBO userBO) throws InstantiationException, IllegalAccessException {
        Long Id = userBO.getId();
        Long curStorage = userBO.getModelCurStorage();
        if (
                Objects.isNull(Id)
                || Objects.isNull(curStorage)
        )
            throw new InvalidParameterException("Invalid parameter");

        return userService.addOnePublicModelStorageById(Id, curStorage);
    }

    @Override
    public Long minusOnePublicModelStorageById(Long id, Long storage) throws InstantiationException, IllegalAccessException {
        if (
                Objects.isNull(id)
                || Objects.isNull(storage)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", id, "storage", storage), UserBO.class);

        return userService.minusOnePublicModelStorageById(id, storage);
    }

    @Override
    public Long minusOnePublicModelStorageById(UserBO userBO) throws InstantiationException, IllegalAccessException {
        Long Id = userBO.getId();
        Long curStorage = userBO.getModelCurStorage();
        if (
                Objects.isNull(Id)
                || Objects.isNull(curStorage)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", Id, "storage", curStorage), UserBO.class);

        return userService.minusOnePublicModelStorageById(Id, curStorage);
    }
}
