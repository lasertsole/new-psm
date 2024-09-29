package com.psm.domain.User.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.User.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDAO;

public interface OAuth2ThirdAccountDB extends IService<OAuth2ThirdAccountDAO> {
    /**
     * 插入第三方用户信息
     *
     * @param oAuth2ThirdAccountDAO 第三方登录用户DAO实体
     */
    void insert(OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO);

    /**
     * 根据第三方用户更新第三方用户信息
     *
     * @param oAuth2ThirdAccountDAO 第三方登录用户DAO实体
     */
    void update(OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO);

    /**
     * 根据主键查询第三方用户信息
     *
     * @param oAuth2ThirdAccountDAO 第三方登录用户DAO实体,应包含主键信息
     * @return 查询到的第三方用户信息
     */
    OAuth2ThirdAccountDAO findByPrimaryKey(OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO);
}
