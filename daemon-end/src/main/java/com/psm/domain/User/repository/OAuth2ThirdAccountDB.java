package com.psm.domain.User.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psm.domain.User.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDAO;

public interface OAuth2ThirdAccountDB extends IService<OAuth2ThirdAccountDAO> {
    /**
     * 插入第三方用户信息
     *
     * @param oAuth2ThirdAccountDAO
     */
    void insert(OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO);

    /**
     * 根据第三方用户信息查询第三方用户信息
     *
     * @param oAuth2ThirdAccountDAO
     * @return
     */
    void update(OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO);

    /**
     * 根据主键查询第三方用户信息
     *
     * @param oAuth2ThirdAccountDAO
     * @return
     */
    OAuth2ThirdAccountDAO findByPrimaryKey(OAuth2ThirdAccountDAO oAuth2ThirdAccountDAO);
}
