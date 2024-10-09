package com.psm.domain.ModelsShowBar.valueObject;

import com.psm.domain.Model.entity.ModelBO;
import com.psm.domain.User.entity.User.UserBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelsShowBarBO {
    private UserBO userBO;
    private List<ModelBO> modelBOs;
}
