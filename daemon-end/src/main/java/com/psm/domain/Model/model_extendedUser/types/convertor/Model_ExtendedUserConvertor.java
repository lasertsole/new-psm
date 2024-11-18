package com.psm.domain.Model.model_extendedUser.types.convertor;

import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model.entity.Model3dDO;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.domain.Model.model.entity.Model3dVO;
import com.psm.domain.Model.model.types.convertor.Model3dConvertor;
import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserBO;
import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserDO;
import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserDTO;
import com.psm.domain.Model.model_extendedUser.valueObject.Model_ExtendedUserVO;
import com.psm.domain.User.relationships.types.convertor.ExtendedUserConvertor;
import com.psm.domain.User.relationships.valueObject.ExtendedUserBO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserDO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserDTO;
import com.psm.domain.User.relationships.valueObject.ExtendedUserVO;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Slf4j
@Mapper
public abstract class Model_ExtendedUserConvertor {
    private static final Model3dConvertor model3dConvertor = Model3dConvertor.INSTANCE;

    private static final ExtendedUserConvertor extendedUserConvertor = ExtendedUserConvertor.INSTANCE;

    public static final Model_ExtendedUserConvertor INSTANCE = Mappers.getMapper(Model_ExtendedUserConvertor.class);

    public Model_ExtendedUserBO DTO2BO(Model_ExtendedUserDTO modelUserBindDTO) {
        ExtendedUserDTO extendedUserDTO = modelUserBindDTO.getUser();
        Model3dDTO model3dDTO = modelUserBindDTO.getModel();

        ExtendedUserBO extendedUserBO = extendedUserConvertor.DTO2BO(extendedUserDTO);
        Model3dBO model3dBO = model3dConvertor.DTO2BO(model3dDTO);

        return new Model_ExtendedUserBO(extendedUserBO, model3dBO);
    }

    public Model_ExtendedUserBO DO2BO(Model_ExtendedUserDO modelUserBindDO) {
        ExtendedUserDO extendedUserDO = modelUserBindDO.getUser();
        Model3dDO model3dDO = modelUserBindDO.getModel();

        ExtendedUserBO extendedUserBO = extendedUserConvertor.DO2BO(extendedUserDO);
        Model3dBO model3dBO = model3dConvertor.DO2BO(model3dDO);

        return new Model_ExtendedUserBO(extendedUserBO, model3dBO);
    }

    public Model_ExtendedUserDTO BO2DTO(Model_ExtendedUserBO modelUserBindBO) {
        ExtendedUserBO extendedUserBO = modelUserBindBO.getUser();
        Model3dBO model3dBO = modelUserBindBO.getModel();

        ExtendedUserDTO extendedUserDTO = extendedUserConvertor.BO2DTO(extendedUserBO);
        Model3dDTO model3dDTO = model3dConvertor.BO2DTO(model3dBO);

        return new Model_ExtendedUserDTO(extendedUserDTO, model3dDTO);
    };

    public Model_ExtendedUserVO DTO2VO(Model_ExtendedUserDTO modelUserBindDTO) {
        ExtendedUserDTO extendedUserDTO = modelUserBindDTO.getUser();
        Model3dDTO model3dDTO = modelUserBindDTO.getModel();

        ExtendedUserVO extendedUserVO = extendedUserConvertor.DTO2VO(extendedUserDTO);
        Model3dVO modelVO = new Model3dVO();

        modelVO.setId(Optional.ofNullable(model3dDTO.getId()).map(Object::toString).orElse(null));
        modelVO.setTitle(model3dDTO.getTitle());
        modelVO.setContent(model3dDTO.getContent());
        modelVO.setCover(model3dDTO.getCover());
        modelVO.setEntity(model3dDTO.getEntity());
        modelVO.setStyle(model3dDTO.getStyle());
        modelVO.setType(model3dDTO.getType());
        modelVO.setCreateTime(model3dDTO.getCreateTime());

        return new Model_ExtendedUserVO(extendedUserVO, modelVO);
    };
}
