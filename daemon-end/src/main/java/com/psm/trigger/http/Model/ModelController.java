package com.psm.trigger.http.Model;

import com.psm.domain.Model.model.adaptor.Model3dAdaptor;
import com.psm.domain.Model.model.entity.Model3dBO;
import com.psm.domain.Model.model.entity.Model3dDTO;
import com.psm.domain.Model.model_extendedUser.adaptor.Model_ExtendedUserAdaptor;
import com.psm.domain.Model.models_user.adaptor.Models_UserAdaptor;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.types.enums.VisibleEnum;
import com.psm.utils.VO.ResponseVO;
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

    /**
     * 模型信息上传接口
     *
     * @param modelDTO 模型信息
     * @return ResponseVO
     */
    @PostMapping("/uploadInfo")
    public ResponseVO uploadModelInfo(Model3dDTO modelDTO){
        Long userId;//当前用户id
        try {
            //获取当前用户id
            userId = userAdaptor.getAuthorizedUserId();
            modelDTO.setUserId(userId);

            // 调用模型信息上传接口,获取上传后的模型id
            Model3dBO modelBO = modelAdaptor.uploadModelInfo(modelDTO);

            // 获取上传后模型大小
            Long modelSize = modelBO.getStorage();

            // 如果模型设置为公开，更新数据库中用户上传公开模型数量+1
            if (Objects.equals(modelBO.getVisible(), VisibleEnum.PUBLIC)) {
                userAdaptor.addOnePublicModelNumById(userId);
            }

            // 增加用户已用的存储空间为当前文件大小
            userAdaptor.addOnePublicModelStorageById(userId, modelSize);
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"upload error:" + e.getCause());
        }

        return ResponseVO.ok("upload model's info successful");
    }

    /**
     * 获取所有公开模型
     *
     * @return ResponseVO
     */
    @GetMapping
    public ResponseVO getModelsShowBars(
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

            return ResponseVO.ok(models_UserAdaptor.getModelsShowBars(current, size, isIdle, canUrgent, style, type, userSelfId));
        }
        catch (InvalidParameterException e) {
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"getModelsShowBar error:" + e);
        }
    }

    /**
     * 获取单个作品模型
     *
     * @param id 作品模型ID
     * @return ResponseVO
     */
    @GetMapping("/{id}")
    public ResponseVO getModelByModelId(@PathVariable Long id) {
        try {
            // 获取当前用户ID
            Long userSelfId = userAdaptor.getAuthorizedUserId();

            return ResponseVO.ok(modelExtendedUserBindAdaptor.getModelByModelId(id, userSelfId));
        }
        catch (InvalidParameterException e) {
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"getModelByModelId error:" + e.getCause());
        }
    }
}
