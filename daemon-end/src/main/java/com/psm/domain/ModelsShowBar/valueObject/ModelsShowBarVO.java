package com.psm.domain.ModelsShowBar.valueObject;

import com.psm.domain.Model.entity.ModelVO;
import com.psm.domain.User.entity.User.UserVO.OtherUserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelsShowBarVO {
    private OtherUserVO userVO;
    private List<ModelVO> modelVOs;
}
