package com.psm.application;

import com.psm.domain.Model.adaptor.ModelAdaptor;
import com.psm.domain.Model.entity.ModelBO;
import com.psm.domain.Model.entity.ModelDTO;
import com.psm.domain.ModelsShowBar.adaptor.ModelsShowBarAdaptor;
import com.psm.domain.ModelsShowBar.valueObject.ModelsShowBarBO;
import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.domain.User.adaptor.UserExtensionAdapter;
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
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private ModelsShowBarAdaptor modelsShowBarAdaptor;

    @RequestMapping(value = {"/upload/**"}, method = {RequestMethod.POST, RequestMethod.PATCH, RequestMethod.HEAD,
            RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.GET})
    @CrossOrigin(exposedHeaders = {"Location", "Upload-Offset", "Upload-Length"})//暴露header
    public ResponseVO uploadModelEntity(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) {
        try {
            //获取当前用户id
            Long userId = userAdaptor.getAuthorizedUserId();

            // 调用模型上传接口
            modelAdaptor.uploadModelEntity(servletRequest, servletResponse, String.valueOf(userId));

            return ResponseVO.ok("upload model's Entity succussful");
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
            List<ModelsShowBarBO> modelsShowBarBOS = modelsShowBarAdaptor.selectModelsShowBarOrderByCreateTimeDesc(pageDTO);
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
            ModelDTO modelDTO = new ModelDTO();
            modelDTO.setId(id);
            modelDTO.setVisible(VisibleEnum.PUBLIC.getValue());
            ModelBO modelBO = modelAdaptor.selectById(modelDTO);

            return ResponseVO.ok(modelBO);
        }
        catch (InvalidParameterException e) {
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"getModelByModelId error:" + e.getCause());
        }
    }
}
