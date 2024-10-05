package com.psm.valueObject;

import com.psm.domain.Subtitles.entity.SubtitlesVO;
import com.psm.domain.User.entity.User.UserBO;
import com.psm.domain.User.entity.User.UserVO.OtherUserVO;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
public class SubtitlesShowBarVO implements Serializable {
    private static final long serialVersionUID = -4862791505815432325L;

    OtherUserVO userVO;
    List<SubtitlesVO> subtitlesVOList;

    public SubtitlesShowBarVO(@Valid UserBO userBO, @Valid List<SubtitlesVO> subtitlesVOList) {
        //校验输入是否合法
        if(
                Objects.isNull(userBO)||
                Objects.isNull(subtitlesVOList)||
                subtitlesVOList.isEmpty()
        ){
            throw new InvalidParameterException("Invalid parameter");
        }

        if(
                Objects.isNull(userVO.getId())&&
                StringUtils.isBlank(userVO.getName())&&
                Objects.isNull(userVO.getAvatar())&&
                Objects.isNull(userVO.getSex())&&
                StringUtils.isBlank(userVO.getName())&&
                StringUtils.isBlank(userVO.getProfile())&&
                StringUtils.isBlank(userVO.getCreateTime())
        )
        {
            throw new InvalidParameterException("Invalid parameter");
        }

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
            {
                throw new InvalidParameterException("Invalid parameter");
            }

            //移除多余信息，减少传输压力
            subtitlesVO.setUserId(null);
        });

        this.userVO = userVO;
        this.subtitlesVOList = subtitlesVOList;
    }
}
