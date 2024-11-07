package com.psm.domain.Model.models_user.types.convertor;

import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.Model.models_user.valueObject.Models_UserBO;
import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.types.convertor.ModelConvertor;
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

    private static final ModelConvertor modelConvertor = ModelConvertor.INSTANCE;

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;

    public static final Models_UserConvertor INSTANCE = Mappers.getMapper(Models_UserConvertor.class);

    public Models_UserBO DAO2BO(Models_UserDAO modelsUserBindDAO) {
        UserDAO userDAO = modelsUserBindDAO.getUser();
        List<ModelDAO> modelDAOs = modelsUserBindDAO.getModels();

        UserBO userBO = userConvertor.DAO2BO(userDAO);
        List<ModelBO> modelBOs = modelDAOs.stream().map(modelConvertor::DAO2BO).toList();

        return new Models_UserBO(userBO, modelBOs);
    }

    public Models_UserVO BO2VO(Models_UserBO modelsUserBindBO) {
        UserBO userBO = modelsUserBindBO.getUser();
        List<ModelBO> modelBOs = modelsUserBindBO.getModels();

        UserVO userVO = userConvertor.BO2OtherVO(userBO);
        List<ModelVO> modelVOs =modelBOs.stream().map((modelBO)->{
            ModelVO modelVO = new ModelVO();

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
