package com.psm.domain.Model.model_extendedUser.types.convertor;

import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.Model.model.types.convertor.ModelConvertor;
import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserBO;
import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserDAO;
import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserVO;
import com.psm.domain.User.follower.types.convertor.ExtendedUserConvertor;
import com.psm.domain.User.follower.valueObject.ExtendedUserBO;
import com.psm.domain.User.follower.valueObject.ExtendedUserDAO;
import com.psm.domain.User.follower.valueObject.ExtendedUserVO;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Slf4j
@Mapper
public abstract class Model_ExtendedUserConvertor {
    private static final ModelConvertor modelConvertor = ModelConvertor.INSTANCE;

    private static final ExtendedUserConvertor extendedUserConvertor = ExtendedUserConvertor.INSTANCE;

    public static final Model_ExtendedUserConvertor INSTANCE = Mappers.getMapper(Model_ExtendedUserConvertor.class);

    public Model_ExtendedUserBO DAO2BO(Model_ExtendedUserDAO modelUserBindDAO) {
        ExtendedUserDAO extendedUserDAO = modelUserBindDAO.getUser();
        ModelDAO modelDAO = modelUserBindDAO.getModel();

        ExtendedUserBO extendedUserBO = extendedUserConvertor.DAO2BO(extendedUserDAO);
        ModelBO modelBO = modelConvertor.DAO2BO(modelDAO);

        return new Model_ExtendedUserBO(extendedUserBO, modelBO);
    }

    public Model_ExtendedUserVO BO2VO(Model_ExtendedUserBO modelUserBindBO) {
        ExtendedUserBO extendedUserBO = modelUserBindBO.getUser();
        ModelBO modelBO = modelUserBindBO.getModel();

        ExtendedUserVO extendedUserVO = extendedUserConvertor.BO2OtherVO(extendedUserBO);
        ModelVO modelVO = new ModelVO();

        modelVO.setId(Optional.ofNullable(modelBO.getId()).map(Object::toString).orElse(null));
        modelVO.setTitle(modelBO.getTitle());
        modelVO.setContent(modelBO.getContent());
        modelVO.setCover(modelBO.getCover());
        modelVO.setEntity(modelBO.getEntity());
        modelVO.setStyle(modelBO.getStyle());
        modelVO.setType(modelBO.getType());
        modelVO.setCreateTime(modelBO.getCreateTime());

        return new Model_ExtendedUserVO(extendedUserVO, modelVO);
    };
}
