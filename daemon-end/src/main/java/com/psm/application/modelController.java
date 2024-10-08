package com.psm.application;

import com.psm.domain.Model.adaptor.ModelAdaptor;
import com.psm.domain.Model.entity.ModelDTO;
import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.domain.User.adaptor.UserExtensionAdapter;
import com.psm.domain.User.entity.UserExtension.UserExtensionBO;
import com.psm.domain.User.entity.UserExtension.UserExtensionDTO;
import com.psm.infrastructure.utils.VO.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;

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
            String userId = String.valueOf(userAdaptor.getAuthorizedUserId());

            // 调用模型上传接口
            modelAdaptor.uploadModelEntity(servletRequest, servletResponse, userId);

            return ResponseVO.ok("upload model's Entity succussful");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"upload error:"+e.getCause());
        }
    }

    @PostMapping("/uploadInfo")
    public ResponseVO uploadModelInfo(ModelDTO modelDTO){
        try {
            //获取当前用户id
            Long UserId = userAdaptor.getAuthorizedUserId();
            modelDTO.setUserId(UserId);

            // 调用模型信息上传接口
            modelAdaptor.uploadModelInfo(modelDTO);

            // 更新数据库中用户上传模型数量+1
            userExtensionAdapter.addOneWorkNumById(new UserExtensionDTO(UserId));

            return ResponseVO.ok("upload model's info succussful");
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"upload error:" + e.getCause());
        }
    }
}
