package com.psm.types.common.ES.BO;

import com.psm.types.common.BO.BO;
import com.psm.types.common.DO.DO;
import com.psm.types.common.DTO.DTO;
import com.psm.types.common.ES.DO.ESResultDO;
import com.psm.types.common.ES.DTO.ESResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESResultBO<T extends BO, E extends DO> implements Serializable, BO<ESResultDTO> {
    private T document;
    private Map<String, List<String>> highlight;

    @Override
    public ESResultDTO toDTO() {
        return new ESResultDTO((DTO) document.toDTO(), highlight);
    }

    public static List<ESResultBO> fromDOList(List<ESResultDO> list) {
        return list.stream().map(ESResultDO::toBO).toList();
    }
}
