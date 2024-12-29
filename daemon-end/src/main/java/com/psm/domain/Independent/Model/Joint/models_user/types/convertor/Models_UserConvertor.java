package com.psm.domain.Independent.Model.Joint.models_user.types.convertor;

import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dBO;
import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dDO;
import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dDTO;
import com.psm.domain.Independent.Model.Single.model3d.types.convertor.Model3dConvertor;
import com.psm.domain.Independent.Model.Joint.models_user.valueObject.Models_UserBO;
import com.psm.domain.Independent.Model.Joint.models_user.valueObject.Models_UserDO;
import com.psm.domain.Independent.Model.Joint.models_user.valueObject.Models_UserDTO;
import com.psm.domain.Independent.User.Single.user.entity.User.UserBO;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDO;
import com.psm.domain.Independent.User.Single.user.entity.User.UserDTO;
import com.psm.domain.Independent.User.Single.user.types.convertor.UserConvertor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    public Models_UserDTO DO2DTO(Models_UserDO modelsUserBindDO) {
        UserDO userDO = modelsUserBindDO.getUser();
        List<Model3dDO> modelDOs = modelsUserBindDO.getModels();

        UserDTO userDTO = userConvertor.DO2OtherDTO(userDO);
        List<Model3dDTO> model3dDTOs = modelDOs.stream().map(model3dConvertor::DO2DTO).toList();

        return new Models_UserDTO(userDTO, model3dDTOs);
    }

    public Models_UserDO BO2DO(Models_UserBO modelsUserBindBO) {
        UserBO userBO = modelsUserBindBO.getUser();
        List<Model3dBO> modelBOs = modelsUserBindBO.getModels();

        UserDO userDO = userConvertor.BO2DO(userBO);
        List<Model3dDO> model3dDOs = modelBOs.stream().map(model3dConvertor::BO2DO).toList();

        return new Models_UserDO(userDO, model3dDOs);
    }

    public Models_UserDTO BO2DTO(Models_UserBO modelsUserBO) {
        UserBO userBO = modelsUserBO.getUser();
        List<Model3dBO> model3dBOs = modelsUserBO.getModels();

        UserDTO userDTO = userConvertor.BO2OtherDTO(userBO);
        List<Model3dDTO> model3dDTOS = model3dBOs.stream().map(model3dConvertor::BO2DTO).toList();

        return new Models_UserDTO(userDTO, model3dDTOS);
    }

    public Models_UserBO DTO2BO(Models_UserDTO modelsUserDTO) {
        UserDTO userDTO = modelsUserDTO.getUser();
        List<Model3dDTO> model3dDTOs = modelsUserDTO.getModels();

        UserBO userBO = userConvertor.DTO2BO(userDTO);
        List<Model3dBO> model3dBOS = model3dDTOs.stream().map(model3dConvertor::DTO2BO).toList();

        return new Models_UserBO(userBO, model3dBOS);
    }
}
