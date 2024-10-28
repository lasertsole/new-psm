package com.psm.domain.Model.modelsUserBind.infrastructure.convertor;

import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindBO;
import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.entity.ModelDAO;
import com.psm.domain.Model.model.infrastructure.convertor.ModelConvertor;
import com.psm.domain.Model.modelsUserBind.entity.BriefModelVO;
import com.psm.domain.User.entity.User.UserVO.OtherUserVO;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindVO;
import com.psm.domain.User.entity.User.UserBO;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.infrastructure.convertor.UserConvertor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Slf4j
@Mapper
public abstract class ModelsUserBindConvertor {
    private static final ModelConvertor modelConvertor = ModelConvertor.INSTANCE;

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;

    public static final ModelsUserBindConvertor INSTANCE = Mappers.getMapper(ModelsUserBindConvertor.class);

    protected UserBO UserDAO2BO(UserDAO userDAO) {
        return userConvertor.DAO2BO(userDAO);
    }

    protected List<ModelBO> ModelsDAO2BO(List<ModelDAO> modelDAOs) {
        return modelDAOs.stream().map(modelConvertor::DAO2BO).toList();
    }

    @Named("UserBO2VO")
    protected OtherUserVO UserBO2VO(UserBO userBO) {
        return userConvertor.BO2VO(userBO);
    }

    @Named("ModelsBO2VO")
    protected List<BriefModelVO> ModelsBO2VO(List<ModelBO> modelBOs) {
        return modelBOs.stream().map((modelBO)->{
            BriefModelVO briefModelVO = new BriefModelVO();

            briefModelVO.setId(modelBO.getId().toString());
            briefModelVO.setTitle(modelBO.getTitle());
            briefModelVO.setCover(modelBO.getCover());
            briefModelVO.setCategory(modelBO.getCategory());
            briefModelVO.setCreateTime(modelBO.getCreateTime());

            return briefModelVO;
        }).toList();
    }

    @Mappings({
            @Mapping(target = "user", qualifiedByName = "UserBO2VO"),
            @Mapping(target = "models", qualifiedByName = "ModelsBO2VO")
    })
    public abstract ModelsUserBindVO BO2VO(ModelsUserBindBO modelsUserBindBO);
}
