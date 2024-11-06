package com.psm.domain.Model.modelExtendedUserBind.types.convertor;

import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.Model.model.types.convertor.ModelConvertor;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindBO;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindDAO;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindVO;
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
public abstract class ModelExtendedUserBindConvertor {
    private static final ModelConvertor modelConvertor = ModelConvertor.INSTANCE;

    private static final ExtendedUserConvertor extendedUserConvertor = ExtendedUserConvertor.INSTANCE;

    public static final ModelExtendedUserBindConvertor INSTANCE = Mappers.getMapper(ModelExtendedUserBindConvertor.class);

    public ModelExtendedUserBindBO DAO2BO(ModelExtendedUserBindDAO modelExtendedUserBindDAO) {
        ExtendedUserDAO extendedUserDAO = modelExtendedUserBindDAO.getUser();
        ModelDAO modelDAO = modelExtendedUserBindDAO.getModel();

        ExtendedUserBO extendedUserBO = extendedUserConvertor.DAO2BO(extendedUserDAO);
        ModelBO modelBO = modelConvertor.DAO2BO(modelDAO);

        return new ModelExtendedUserBindBO(extendedUserBO, modelBO);
    }

    public ModelExtendedUserBindVO BO2VO(ModelExtendedUserBindBO modelUserBindBO) {
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

        return new ModelExtendedUserBindVO(extendedUserVO, modelVO);
    };
}
