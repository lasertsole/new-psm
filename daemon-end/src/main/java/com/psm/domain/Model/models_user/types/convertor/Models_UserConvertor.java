package com.psm.domain.Model.models_user.types.convertor;

import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model.entity.Model3dDAO;
import com.psm.domain.Model.model.entity.Model3dVO;
import com.psm.domain.Model.model.types.convertor.Model3dConvertor;
import com.psm.domain.Model.models_user.valueObject.Models_UserBO;
import com.psm.domain.Model.models_user.valueObject.Models_UserDAO;
import com.psm.domain.Model.models_user.valueObject.Models_UserVO;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.domain.User.user.entity.User.UserVO;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

@Slf4j
@Mapper
public abstract class Models_UserConvertor {

    private static final Model3dConvertor model3dConvertor = Model3dConvertor.INSTANCE;

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;

    public static final Models_UserConvertor INSTANCE = Mappers.getMapper(Models_UserConvertor.class);

    public Models_UserBO DAO2BO(Models_UserDAO modelsUserBindDAO) {
        UserDAO userDAO = modelsUserBindDAO.getUser();
        List<Model3dDAO> modelDAOs = modelsUserBindDAO.getModels();

        UserBO userBO = userConvertor.DAO2BO(userDAO);
        List<Model3dBO> modelBOs = modelDAOs.stream().map(model3dConvertor::DAO2BO).toList();

        return new Models_UserBO(userBO, modelBOs);
    }

    public Models_UserVO BO2VO(Models_UserBO modelsUserBindBO) {
        UserBO userBO = modelsUserBindBO.getUser();
        List<Model3dBO> modelBOs = modelsUserBindBO.getModels();

        UserVO userVO = userConvertor.BO2OtherVO(userBO);
        List<Model3dVO> modelVOs =modelBOs.stream().map((modelBO)->{
            Model3dVO modelVO = new Model3dVO();

            modelVO.setId(Optional.ofNullable(modelBO.getId()).map(Object::toString).orElse(null));
            modelVO.setTitle(modelBO.getTitle());
            modelVO.setCover(modelBO.getCover());
            modelVO.setStyle(modelBO.getStyle());
            modelVO.setType(modelBO.getType());
            modelVO.setCreateTime(modelBO.getCreateTime());

            return modelVO;
        }).toList();

        return new Models_UserVO(userVO, modelVOs);
    };
}
