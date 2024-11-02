package com.psm.domain.Model.modelUserBind.infrastructure.convertor;

import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.Model.model.infrastructure.convertor.ModelConvertor;
import com.psm.domain.Model.modelUserBind.valueObject.ModelUserBindBO;
import com.psm.domain.Model.modelUserBind.valueObject.ModelUserBindVO;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserVO;
import com.psm.domain.User.user.infrastructure.convertor.UserConvertor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Slf4j
@Mapper
public abstract class ModelUserBindConvertor {
    private static final ModelConvertor modelConvertor = ModelConvertor.INSTANCE;

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;

    public static final ModelUserBindConvertor INSTANCE = Mappers.getMapper(ModelUserBindConvertor.class);

    public ModelUserBindVO BO2VO(ModelUserBindBO modelUserBindBO) {
        UserBO userBO = modelUserBindBO.getUser();
        ModelBO modelBO = modelUserBindBO.getModel();

        UserVO userVO = userConvertor.BO2OtherVO(userBO);
        ModelVO modelVO = new ModelVO();

        modelVO.setId(modelBO.getId().toString());
        modelVO.setTitle(modelBO.getTitle());
        modelVO.setContent(modelBO.getContent());
        modelVO.setCover(modelBO.getCover());
        modelVO.setEntity(modelBO.getEntity());
        modelVO.setCategory(modelBO.getCategory());
        modelVO.setCreateTime(modelBO.getCreateTime());

        return new ModelUserBindVO(userVO, modelVO);
    };
}
