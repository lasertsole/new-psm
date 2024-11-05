package com.psm.domain.Model.modelsUserBind.types.convertor;

import com.psm.domain.Model.model.entity.ModelVO;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindBO;
import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.types.convertor.ModelConvertor;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindVO;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserVO;
import com.psm.domain.User.user.types.convertor.UserConvertor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Slf4j
@Mapper
public abstract class ModelsUserBindConvertor {
    private static final ModelConvertor modelConvertor = ModelConvertor.INSTANCE;

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;

    public static final ModelsUserBindConvertor INSTANCE = Mappers.getMapper(ModelsUserBindConvertor.class);

    public ModelsUserBindVO BO2VO(ModelsUserBindBO modelsUserBindBO) {
        UserBO userBO = modelsUserBindBO.getUser();
        List<ModelBO> modelBOs = modelsUserBindBO.getModels();

        UserVO userVO = userConvertor.BO2OtherVO(userBO);
        List<ModelVO> modelVOs =modelBOs.stream().map((modelBO)->{
            ModelVO modelVO = new ModelVO();

            modelVO.setId(modelBO.getId().toString());
            modelVO.setTitle(modelBO.getTitle());
            modelVO.setCover(modelBO.getCover());
            modelVO.setStyle(modelBO.getStyle());
            modelVO.setType(modelBO.getType());
            modelVO.setCreateTime(modelBO.getCreateTime());

            return modelVO;
        }).toList();

        return new ModelsUserBindVO(userVO, modelVOs);
    };
}
