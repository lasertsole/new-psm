package com.psm.trigger.http.Model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Model.model.adaptor.ModelAdaptor;
import com.psm.domain.Model.model.entity.ModelBO;
import com.psm.domain.Model.model.entity.ModelDTO;
import com.psm.domain.Model.modelExtendedUserBind.adaptor.ModelExtendedUserBindAdaptor;
import com.psm.domain.Model.modelExtendedUserBind.valueObject.ModelExtendedUserBindBO;
import com.psm.domain.Model.modelsUserBind.adaptor.ModelsUserBindAdaptor;
import com.psm.domain.Model.modelsUserBind.valueObject.ModelsUserBindBO;
import com.psm.domain.User.follower.adaptor.FollowerAdaptor;
import com.psm.domain.User.follower.valueObject.ExtendedUserBO;
import com.psm.domain.User.user.adaptor.UserAdaptor;
import com.psm.domain.User.user.adaptor.UserExtensionAdapter;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionBO;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDTO;
import com.psm.types.enums.VisibleEnum;
import com.psm.types.utils.page.PageDTO;
import com.psm.types.utils.VO.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    private ModelsUserBindAdaptor modelsUserBindAdaptor;

    @Autowired
    private ModelExtendedUserBindAdaptor modelExtendedUserBindAdaptor;

    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private UserExtensionAdapter userExtensionAdapter;

    @Autowired
    private ModelAdaptor modelAdaptor;

    @Autowired
    private FollowerAdaptor followerAdaptor;

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

            // 如果模型设置为公开，更新数据库中用户上传公开模型数量+1
            if (Objects.equals(modelBO.getVisible(), VisibleEnum.PUBLIC)) {
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
    ) {
        try {
            Long userSelfId = null;
            if (Objects.nonNull(isFollowing) && isFollowing) userSelfId = userAdaptor.getAuthorizedUserId();
            return ResponseVO.ok(modelsUserBindAdaptor.getModelsShowBars(current, size, isIdle, canUrgent, style, type, userSelfId));
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
