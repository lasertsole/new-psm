package com.psm.application;

import com.psm.domain.Subtitles.adaptor.SubtitlesAdaptor;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.domain.Subtitles.entity.SubtitlesVO;
import com.psm.domain.User.entity.User.UserBO;
import com.psm.domain.SubtitlesShowBar.valueObject.SubtitlesShowBarVO;
import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.domain.User.entity.User.UserDTO;
import com.psm.infrastructure.utils.MybatisPlus.PageDTO;
import com.psm.infrastructure.utils.VO.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subtitlesShowBars")
public class SubtitlesShowBarController {
    @Autowired
    UserAdaptor userAdaptor;

    @Autowired
    SubtitlesAdaptor subtitlesAdaptor;

    @GetMapping
    public ResponseVO getSubtitlesShowBars(PageDTO pageDTO){
        // 获取用户列表
        List<UserBO> userBOs = userAdaptor.getUserOrderByCreateTimeAsc(pageDTO);
        if(userBOs == null){
            return new ResponseVO(HttpStatus.NOT_FOUND, "Get user list failed");
        }

        // 创建字幕Bar列表
        List<SubtitlesShowBarVO> subtitlesShowBarVOList =  new ArrayList<>(10);

        // 创建字幕列表
        List<SubtitlesVO> subtitlesVOList;

        // 判断用户字幕列表是否为空
        Boolean emptyFlag = true;

        // 遍历用户列表
        for (UserBO userBO : userBOs) {
            // 创建字幕DTO
            SubtitlesDTO subtitlesDTO = new SubtitlesDTO();
            subtitlesDTO.setUserId(userBO.getId());
            try {
                // 获取用户字幕
                subtitlesVOList = subtitlesAdaptor.getSubtitlesByUserId(subtitlesDTO);

                // 创建字幕Bar
                SubtitlesShowBarVO subtitlesShowBarVO = new SubtitlesShowBarVO(userBO, subtitlesVOList);

                // 将赋值好的字幕Bar添加到列表
                subtitlesShowBarVOList.add(subtitlesShowBarVO);

                // 设置非空标志
                emptyFlag = false;
            } catch (Exception e) {
                continue; // 遇到异常跳过本次循环
            }
        }

        // 判断用户字幕列表是否为空
        if (emptyFlag){
            return new ResponseVO(HttpStatus.NOT_FOUND, "Get getSubtitlesShowBars failed");
        }

        // 返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("subtitlesShowBars", subtitlesShowBarVOList);
        return new ResponseVO(HttpStatus.OK, "Get getSubtitlesShowBars successful", map);
    }

    @GetMapping({"/{userId}"})
    public ResponseVO getSubtitlesShowBarById(Long userId){
        // 获取用户
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        UserBO userBO = userAdaptor.getUserByID(userDTO);
        if(userBO == null){
            return new ResponseVO(HttpStatus.NOT_FOUND, "Get user list failed");
        }

        // 获取用户字幕列表
        SubtitlesDTO subtitlesDTO = new SubtitlesDTO();
        subtitlesDTO.setUserId(userId);
        List<SubtitlesVO> subtitlesVOList = subtitlesAdaptor.getSubtitlesByUserId(subtitlesDTO);

        if (subtitlesVOList.isEmpty()){
            return new ResponseVO(HttpStatus.NOT_FOUND, "Get subtitles list failed");
        }

        SubtitlesShowBarVO subtitlesShowBarVO = new SubtitlesShowBarVO(userBO, subtitlesVOList);

        // 返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("subtitlesShowBar", subtitlesShowBarVO);
        return new ResponseVO(HttpStatus.OK, "Get getSubtitlesShowBar successful", map);
    }
}
