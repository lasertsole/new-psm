package com.psm.application;

import com.psm.domain.Model.adaptor.ModelAdaptor;
import com.psm.domain.Model.entity.ModelDTO;
import com.psm.domain.ModelsShowBar.adaptor.ModelsShowBarAdaptor;
import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;
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
    private ModelAdaptor modelAdaptor;

    @Autowired
    private ModelsShowBarAdaptor modelsShowBarAdaptor;

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
        Long userId;//当前用户id
        try {
            //获取当前用户id
            userId = userAdaptor.getAuthorizedUserId();
            modelDTO.setUserId(userId);

            // 调用模型信息上传接口
            modelAdaptor.uploadModelInfo(modelDTO);
        }
        catch (InvalidParameterException e){
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"upload error:" + e.getCause());
        }

        return ResponseVO.ok("upload model's info succussful");
    }

    @GetMapping
    public ResponseVO getModelsShowBar(
            @ModelAttribute PageDTO pageDTO
        ){
        try {
            return ResponseVO.ok(modelsShowBarAdaptor.selectModelsShowBarOrderByCreateTimeDesc(pageDTO));
        }
        catch (InvalidParameterException e) {
            return new ResponseVO(HttpStatus.BAD_REQUEST,"InvalidParameterException");
        }
        catch (Exception e){
            return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR,"getModelsShowBar error:" + e.getCause());
        }
    }

}
