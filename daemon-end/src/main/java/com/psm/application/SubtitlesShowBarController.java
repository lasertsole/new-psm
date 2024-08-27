package com.psm.application;

import com.psm.domain.Subtitles.adaptor.SubtitlesAdaptor;
import com.psm.domain.Subtitles.entity.SubtitlesDTO;
import com.psm.domain.Subtitles.entity.SubtitlesVO;
import com.psm.domain.SubtitlesShowBar.adaptor.SubtitlesShowBarAdaptor;
import com.psm.domain.SubtitlesShowBar.entity.SubtitlesShowBarVO;
import com.psm.domain.User.adaptor.UserAdaptor;
import com.psm.domain.User.entity.User.UserVO;
import com.psm.utils.DTO.PageDTO;
import com.psm.utils.DTO.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseDTO getSubtitlesShowBars(PageDTO pageDTO){
        // 获取用户列表
        List<UserVO> userVOList = userAdaptor.getUserOrderByCreateTimeAsc(pageDTO);
        if(userVOList == null){
            return null;
        }

        // 创建字幕Bar列表
        List<SubtitlesShowBarVO> subtitlesShowBarVOList = List.of();

        // 创建字幕列表
        List<SubtitlesVO> subtitlesVOList;

        // 遍历用户列表
        for (UserVO userVO : userVOList) {
            // 创建字幕DTO
            SubtitlesDTO subtitlesDTO = new SubtitlesDTO();
            subtitlesDTO.setUserId(userVO.getId());
            try {
                // 获取用户字幕
                subtitlesVOList = subtitlesAdaptor.getSubtitlesByUserId(subtitlesDTO);

                // 创建字幕Bar
                SubtitlesShowBarVO subtitlesShowBarVO = new SubtitlesShowBarVO(userVO, subtitlesVOList);

                // 将赋值好的字幕Bar添加到列表
                subtitlesShowBarVOList.add(subtitlesShowBarVO);
            } catch (Exception e) {
                continue; // 遇到异常跳过本次循环
            }
        }

        // 返回数据
        Map<String, Object> map = new HashMap<>();
        map.put("subtitlesShowBars", subtitlesShowBarVOList);
        return new ResponseDTO(HttpStatus.OK, "Get getSubtitlesShowBars successful", map);
    }
}
