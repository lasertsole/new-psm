package com.psm.domain.Model.modelUserBind.infrastructure.convertor;

import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.infrastructure.convertor.ModelConvertor;
import com.psm.domain.Model.modelUserBind.valueObject.ModelUserBindBO;
import com.psm.domain.Model.modelUserBind.valueObject.ModelUserBindVO;
import com.psm.domain.Model.modelUserBind.entity.BriefModelVO;
import com.psm.domain.User.entity.User.UserBO;
import com.psm.domain.User.entity.User.UserVO.OtherUserVO;
import com.psm.domain.User.infrastructure.convertor.UserConvertor;
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

    @Named("UserBO2VO")
    protected OtherUserVO UserBO2VO(UserBO userBO) {
        return userConvertor.BO2VO(userBO);
    }

    @Named("ModelBO2VO")
    protected BriefModelVO ModelsBO2VO(ModelBO modelBO) {
        BriefModelVO briefModelVO = new BriefModelVO();

        briefModelVO.setId(modelBO.getId().toString());
        briefModelVO.setTitle(modelBO.getTitle());
        briefModelVO.setContent(modelBO.getContent());
        briefModelVO.setCover(modelBO.getCover());
        briefModelVO.setEntity(modelBO.getEntity());
        briefModelVO.setCategory(modelBO.getCategory());
        briefModelVO.setCreateTime(modelBO.getCreateTime());

        return briefModelVO;
    }

    @Mappings({
            @Mapping(target = "user", qualifiedByName = "UserBO2VO"),
            @Mapping(target = "model", qualifiedByName = "ModelBO2VO")
    })
    public abstract ModelUserBindVO BO2VO(ModelUserBindBO modelUserBindBO);
}
