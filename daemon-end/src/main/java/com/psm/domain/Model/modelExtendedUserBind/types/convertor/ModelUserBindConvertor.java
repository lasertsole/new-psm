package com.psm.domain.Model.modelExtendedUserBind.types.convertor;

import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.Model.model.types.convertor.ModelConvertor;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindBO;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindVO;
import com.psm.domain.User.follower.types.convertor.ExtendedUserConvertor;
import com.psm.domain.User.follower.valueObject.ExtendedUserBO;
import com.psm.domain.User.follower.valueObject.ExtendedUserVO;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Slf4j
@Mapper
public abstract class ModelUserBindConvertor {
    private static final ModelConvertor modelConvertor = ModelConvertor.INSTANCE;

    private static final ExtendedUserConvertor extendedUserConvertor = ExtendedUserConvertor.INSTANCE;

    public static final ModelUserBindConvertor INSTANCE = Mappers.getMapper(ModelUserBindConvertor.class);

    public ModelExtendedUserBindVO BO2VO(ModelExtendedUserBindBO modelUserBindBO) {
        ExtendedUserBO extendedUserBO = modelUserBindBO.getUser();
        ModelBO modelBO = modelUserBindBO.getModel();

        ExtendedUserVO userVO = extendedUserConvertor.BO2OtherVO(extendedUserBO);
        ModelVO modelVO = new ModelVO();

        modelVO.setId(modelBO.getId().toString());
        modelVO.setTitle(modelBO.getTitle());
        modelVO.setContent(modelBO.getContent());
        modelVO.setCover(modelBO.getCover());
        modelVO.setEntity(modelBO.getEntity());
        modelVO.setStyle(modelBO.getStyle());
        modelVO.setType(modelBO.getType());
        modelVO.setCreateTime(modelBO.getCreateTime());

        return new ModelExtendedUserBindVO(userVO, modelVO);
    };
}
