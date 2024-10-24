package com.psm.domain.ModelsShowBar.infrastructure.convertor;

import com.psm.domain.Model.entity.ModelBO;
import com.psm.domain.Model.entity.ModelDAO;
import com.psm.domain.Model.infrastructure.convertor.ModelConvertor;
import com.psm.domain.ModelsShowBar.entity.BriefModelVO;
import com.psm.domain.User.entity.User.UserVO.OtherUserVO;
import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarBO;
import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarDAO;
import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarVO;
import com.psm.domain.User.entity.User.UserBO;
import com.psm.domain.User.entity.User.UserDAO;
import com.psm.domain.User.infrastructure.convertor.UserConvertor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Slf4j
@Mapper
public abstract class ModelsShowBarConvertor {
    private static final ModelConvertor modelConvertor = ModelConvertor.INSTANCE;

    private static final UserConvertor userConvertor = UserConvertor.INSTANCE;

    public static final ModelsShowBarConvertor INSTANCE = Mappers.getMapper(ModelsShowBarConvertor.class);

    protected UserBO UserDAO2BO(UserDAO userDAO) {
        return userConvertor.DAO2BO(userDAO);
    }

    protected List<ModelBO> ModelsDAO2BO(List<ModelDAO> modelDAOs) {
        return modelDAOs.stream().map(modelConvertor::DAO2BO).toList();
    }

    public ModelsShowBarBO DAO2BO(ModelsShowBarDAO modelsShowBarDAO) { //值对象只能通过构造方法赋值，不能通过set方法赋值, 所以需要手动赋值
        UserDAO userDAO = modelsShowBarDAO.getUser();
        List<ModelDAO> modelDAOs = modelsShowBarDAO.getModels();

        UserBO userBO = UserDAO2BO(userDAO);
        List<ModelBO> modelBOs = ModelsDAO2BO(modelDAOs);

        return new ModelsShowBarBO(userBO, modelBOs);
    };

    @Named("UserBO2VO")
    protected OtherUserVO UserBO2VO(UserBO userBO) {
        return userConvertor.BO2VO(userBO);
    }

    @Named("ModelsBO2VO")
    protected List<BriefModelVO> ModelsBO2VO(List<ModelBO> modelBOs) {
        return modelBOs.stream().map((modelBO)->{
            BriefModelVO briefModelVO = new BriefModelVO();
            BeanUtils.copyProperties(modelBO, briefModelVO);
            return briefModelVO;
        }).toList();
    }

    @Mappings({
            @Mapping(target = "user", qualifiedByName = "UserBO2VO"),
            @Mapping(target = "models", qualifiedByName = "ModelsBO2VO")
    })
    public abstract ModelsShowBarVO BO2VO(ModelsShowBarBO modelsShowBarBO);
}
