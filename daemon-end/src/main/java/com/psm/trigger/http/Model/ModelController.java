package com.psm.trigger.http.Model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Model.Single.model3d.adaptor.Model3dAdaptor;
import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dBO;
import com.psm.domain.Independent.Model.Single.model3d.entity.Model3dDTO;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.adaptor.Model_ExtendedUserAdaptor;
import com.psm.domain.Independent.Model.Joint.model_extendedUser.valueObject.Model_ExtendedUserBO;
import com.psm.domain.Independent.Model.Joint.models_user.adaptor.Models_UserAdaptor;
import com.psm.domain.Independent.Model.Joint.models_user.valueObject.Models_UserBO;
import com.psm.domain.Independent.User.Single.user.adaptor.UserAdaptor;
import com.psm.types.common.DTO.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/models")
public class ModelController {
    @Autowired
    private Models_UserAdaptor models_UserAdaptor;

    @Autowired
    private Model_ExtendedUserAdaptor modelExtendedUserBindAdaptor;

    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private Model3dAdaptor modelAdaptor;

    /**
     * 模型上传接口
     *
     * @param servletRequest HTTP request objects.
     * @param servletResponse HTTP response objects.
     * @return ResponseVO
     */
    @RequestMapping(value = {"/upload/**"}, method = {RequestMethod.POST, RequestMethod.PATCH, RequestMethod.HEAD,
            RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.GET})
    @CrossOrigin(exposedHeaders = {"Location", "Upload-Offset", "Upload-Length"})//暴露header
    public ResponseDTO uploadModel3dEntity(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) {
        try {
            //获取当前用户id
            Long userId = userAdaptor.getAuthorizedUserId();

            // 调用模型上传接口
            modelAdaptor.uploadModelEntity(servletRequest, servletResponse, String.valueOf(userId));

            return ResponseDTO.ok("upload model's Entity successful");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"upload error:"+e.getCause());
        }
    }

    /**
     * 模型信息上传接口
     *
     * @param model3dDTO 模型信息
     * @return ResponseVO
     */
    @PostMapping("/uploadInfo")
    public ResponseDTO uploadModel3dInfo(Model3dDTO model3dDTO) {
        Long userId;//当前用户id
        try {
            //获取当前用户id
            userId = userAdaptor.getAuthorizedUserId();
            Model3dBO model3dBO = Model3dBO.from(model3dDTO);
            model3dBO.setUserId(userId);

            // 模型信息上传
            modelAdaptor.uploadModelInfo(model3dBO);
        } catch (InvalidParameterException e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"upload error:" + e.getCause());
        }

        return ResponseDTO.ok("upload model's info successful");
    }

    /**
     * 获取所有公开模型
     *
     * @return ResponseVO
     */
    @GetMapping
    public ResponseDTO getModel3dsShowBars(
            @RequestParam Integer current,
            @RequestParam Integer size,
            @RequestParam(required = false) Boolean isIdle,
            @RequestParam(required = false) Boolean canUrgent,
            @RequestParam(required = false) String style,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean isFollowing
    )  {
        Long userSelfId = null;

        try {
            if (Objects.nonNull(isFollowing) && isFollowing) userSelfId = userAdaptor.getAuthorizedUserId();

            Page<Models_UserBO> modelsUserBOPage = models_UserAdaptor.getModelsShowBars(current, size, isIdle, canUrgent, style, type, userSelfId);

            return ResponseDTO.ok(modelsUserBOPage);
        } catch (InvalidParameterException e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"getModelsShowBar error:" + e);
        }
    }

    /**
     * 获取单个作品模型
     *
     * @param id 作品模型ID
     * @return ResponseVO
     */
    @GetMapping("/{id}")
    public ResponseDTO getModel3dByModelId(@PathVariable Long id) {
        try {
            // 获取当前用户ID
            Long userSelfId = userAdaptor.getAuthorizedUserId();

            Model_ExtendedUserBO model_ExtendedUserBO = modelExtendedUserBindAdaptor.getModelByModelId(id, userSelfId);

            return ResponseDTO.ok(model_ExtendedUserBO);
        } catch (InvalidParameterException e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"getModelByModelId error:" + e.getCause());
        }
    }

    @GetMapping("/blurSearch")
    public ResponseDTO getBlurSearchModel3d(@RequestParam String keyword) {
        try {
            return ResponseDTO.ok(modelAdaptor.getBlurSearchModel3d(keyword));
        } catch (InvalidParameterException e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"getBlurSearchModel3d error:" + e);
        }
    }

    @GetMapping("/detailSearch")
    public ResponseDTO getDetailSearchModel3d(
            @RequestParam String keyword,
            @RequestParam String afterKeyId,
            @RequestParam Integer size) {
        try {
            return ResponseDTO.ok(modelAdaptor.getDetailSearchModel3d(keyword, afterKeyId, size));
        } catch (InvalidParameterException e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        } catch (Exception e){
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"getDetailSearchModel3d error:" + e);
        }
    }
}
