package com.psm.application;

import com.psm.domain.Model.model.adaptor.ModelAdaptor;
import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.entity.ModelDTO;
import com.psm.domain.Model.modelUserBind.valueObject.ModelUserBindBO;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindBO;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.adaptor.UserExtensionAdapter;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionBO;
import com.psm.infrastructure.enums.VisibleEnum;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;
import com.psm.infrastructure.utils.VO.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/models")
public class ModelController {
    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private UserExtensionAdapter userExtensionAdapter;

    @Autowired
    private ModelAdaptor modelAdaptor;

    @RequestMapping(value = {"/upload/**"}, method = {RequestMethod.POST, RequestMethod.PATCH, RequestMethod.HEAD,
            RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.GET})
    @CrossOrigin(exposedHeaders = {"Location", "Upload-Offset", "Upload-Length"})//暴露header
    public ResponseVO uploadModelEntity(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) {
        try {
            //获取当前用户id
            Long userId = userAdaptor.getAuthorizedUserId();

            // 调用模型上传接口
            modelAdaptor.uploadModelEntity(servletRequest, servletResponse, String.valueOf(userId));

            return ResponseVO.ok("upload model's Entity successful");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"upload error:"+e.getCause());
        }
    }

    @PostMapping("/uploadInfo")
    public ResponseVO uploadModelInfo(ModelDTO modelDTO){
        Long userId;//当前用户id
        try {
            //获取当前用户id
            userId = userAdaptor.getAuthorizedUserId();
            modelDTO.setUserId(userId);

            // 调用模型信息上传接口,获取上传后的模型id
            ModelBO modelBO = modelAdaptor.uploadModelInfo(modelDTO);

            // 获取上传后模型大小
            Long modelSize = modelBO.getStorage();

            // 如果模型设置为公开，更新数据库中用户上传公开模型数量+1,
            if (Objects.equals(modelBO.getVisible(), VisibleEnum.PUBLIC.getValue())) {
                userExtensionAdapter.addOnePublicModelNumById(userId);
            }

            // 增加用户已用的存储空间为当前文件大小
            userExtensionAdapter.addOneModelStorageById(userId, modelSize);
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"upload error:" + e.getCause());
        }

        return ResponseVO.ok("upload model's info successful");
    }

    @GetMapping
    public ResponseVO getModelsShowBars(@ModelAttribute PageDTO pageDTO) {
        try {
            // 按照页配置获取发过模型的用户的ID列表,并按时间降序排序
            List<UserExtensionBO> userExtensionBOResultList = userExtensionAdapter.getHasPublicModelOrderByCreateTimeDesc(pageDTO);

            // 如果用户列表为空，则返回空列表
            if (userExtensionBOResultList.isEmpty()) return ResponseVO.ok(Collections.emptyList());

            // 创建一个Map，用于存储用户ID和ModelsShowBarDAO的映射
            Map<Long, ModelsUserBindBO> collect = userExtensionBOResultList.stream().collect(Collectors.toMap(
                    UserExtensionBO::getId, userExtensionDAO -> new ModelsUserBindBO()
            ));

            // 获取用户ID列表，这个ID列表是按照时间降序排序的
            List<Long> userIds = UserExtensionBO.getModelIds(userExtensionBOResultList);

            // 按照用户ID列表获取用户列表
            List<UserBO> userBOList = userAdaptor.getUserByIds(userIds);
            userBOList.forEach(userBO -> {
                ModelsUserBindBO modelsShowBarBO = collect.get(userBO.getId());
                modelsShowBarBO.setUser(userBO);
                modelsShowBarBO.setModels(new ArrayList<ModelBO>());
            });

            // 按照用户ID列表获取作品模型列表
            List<ModelBO> modelBOList = modelAdaptor.getByUserIds(userIds, VisibleEnum.PUBLIC);

            // 将作品模型列表添加到对应的ModelsShowBarDAO中
            modelBOList.forEach(modelBO -> {
                ModelsUserBindBO modelsShowBarBO = collect.get(modelBO.getUserId());
                modelsShowBarBO.getModels().add(modelBO);
            });

            List<ModelsUserBindBO> modelsShowBarBOS = collect.values().stream().toList();

            return ResponseVO.ok(modelsShowBarBOS);
        }
        catch (InvalidParameterException e) {
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"getModelsShowBar error:" + e.getCause());
        }
    }

    @GetMapping("/{id}")
    public ResponseVO getModelByModelId(@PathVariable Long id) {
        try {
            // 获取模型
            ModelDTO modelDTO = new ModelDTO();
            modelDTO.setId(id);
            modelDTO.setVisible(VisibleEnum.PUBLIC.getValue());
            ModelBO modelBO = modelAdaptor.selectById(modelDTO);

            // 获取用户信息
            Long userId = modelBO.getUserId();
            UserBO userBO = userAdaptor.getUserById(userId);

            return ResponseVO.ok(new ModelUserBindBO(userBO, modelBO));
        }
        catch (InvalidParameterException e) {
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"getModelByModelId error:" + e.getCause());
        }
    }
}
