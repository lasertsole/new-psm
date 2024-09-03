package com.psm.domain.User.infrastructure.Convertor;

import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.domain.User.entity.User.UserVO;
import org.springframework.beans.BeanUtils;

public class UserConvertor {


    public static UserDAO DTOConvertToDAO(UserDTO userDTO){
        UserDAO userDAO = new UserDAO();
        BeanUtils.copyProperties(userDTO, userDAO);

        return userDAO;
    }

    public static UserVO DAOConvertToVO(UserDAO userDAO){
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDAO, userVO);

        return userVO;
    }
}
