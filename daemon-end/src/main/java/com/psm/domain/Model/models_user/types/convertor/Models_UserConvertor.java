package com.psm.domain.Model.models_user.types.convertor;

import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model.entity.Model3dDO;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.domain.Model.model.entity.Model3dVO;
import com.psm.domain.Model.model.types.convertor.Model3dConvertor;
import com.psm.domain.Model.models_user.valueObject.Models_UserBO;
import com.psm.domain.Model.models_user.valueObject.Models_UserDO;
import com.psm.domain.Model.models_user.valueObject.Models_UserDTO;
import com.psm.domain.Model.models_user.valueObject.Models_UserVO;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.User.UserDO;
import com.psm.domain.User.user.entity.User.UserDTO;
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

    public Models_UserBO DO2BO(Models_UserDO modelsUserBindDO) {
        UserDO userDO = modelsUserBindDO.getUser();
        List<Model3dDO> modelDOs = modelsUserBindDO.getModels();

        UserBO userBO = userConvertor.DO2BO(userDO);
        List<Model3dBO> model3dBOs = modelDOs.stream().map(model3dConvertor::DO2BO).toList();

        return new Models_UserBO(userBO, model3dBOs);
    }

    public Models_UserDTO BO2DTO(Models_UserBO modelsUserBO) {
        UserBO userBO = modelsUserBO.getUser();
        List<Model3dBO> model3dBOs = modelsUserBO.getModels();

        UserDTO userDTO = userConvertor.BO2DTO(userBO);
        List<Model3dDTO> model3dDTOS = model3dBOs.stream().map(model3dConvertor::BO2DTO).toList();

        return new Models_UserDTO(userDTO, model3dDTOS);
    }

    public Models_UserVO DTO2VO(Models_UserDTO models_UserDTO) {
        UserDTO userDTO = models_UserDTO.getUser();
        List<Model3dDTO> model3dDTOs = models_UserDTO.getModels();

        UserVO userVO = userConvertor.DTO2OtherVO(userDTO);
        List<Model3dVO> modelVOs =model3dDTOs.stream().map((modelBO)->{
            Model3dVO model3dVO = new Model3dVO();

            model3dVO.setId(Optional.ofNullable(modelBO.getId()).map(Object::toString).orElse(null));
            model3dVO.setTitle(modelBO.getTitle());
            model3dVO.setCover(modelBO.getCover());
            model3dVO.setStyle(modelBO.getStyle());
            model3dVO.setType(modelBO.getType());
            model3dVO.setCreateTime(modelBO.getCreateTime());

            return model3dVO;
        }).toList();

        return new Models_UserVO(userVO, modelVOs);
    };
}
