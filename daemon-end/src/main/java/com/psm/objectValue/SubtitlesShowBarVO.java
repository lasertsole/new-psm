package com.psm.objectValue;

import com.psm.domain.Subtitles.entity.SubtitlesVO;
import com.psm.domain.User.entity.User.UserVO;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.Getter;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;

@Getter
public class SubtitlesShowBarVO implements Serializable {
    private static final long serialVersionUID = -7834822399598777861L;

    UserVO userVO;
    List<SubtitlesVO> subtitlesVOList;

    public SubtitlesShowBarVO(@Valid UserVO userVO, @Valid List<SubtitlesVO> subtitlesVOList) {
        //校验输入是否合法
        if(
                Objects.isNull(userVO)||
                Objects.isNull(subtitlesVOList)||
                subtitlesVOList.isEmpty()
        )
            throw new InvalidParameterException("Invalid parameter");

        if(
                Objects.isNull(userVO.getId())&&
                StringUtils.isBlank(userVO.getName())&&
                Objects.isNull(userVO.getAvatar())&&
                Objects.isNull(userVO.getSex())&&
                StringUtils.isBlank(userVO.getName())&&
                StringUtils.isBlank(userVO.getProfile())&&
                StringUtils.isBlank(userVO.getCreateTime())
        )
            throw new InvalidParameterException("Invalid parameter");

        subtitlesVOList.forEach(subtitlesVO -> {
            if(
                    Objects.isNull(subtitlesVO.getId())&&
                    Objects.isNull(subtitlesVO.getTitle())&&
                    Objects.isNull(subtitlesVO.getContent())&&
                    StringUtils.isBlank(subtitlesVO.getCover())&&
                    StringUtils.isBlank(subtitlesVO.getVideo())&&
                    StringUtils.isBlank(subtitlesVO.getCategory())&&
                    StringUtils.isBlank(subtitlesVO.getCreateTime())
            )
                throw new InvalidParameterException("Invalid parameter");

            //移除多余信息，减少传输压力
            subtitlesVO.setUserId(null);
        });

        this.userVO = userVO;
        this.subtitlesVOList = subtitlesVOList;
    }
}
