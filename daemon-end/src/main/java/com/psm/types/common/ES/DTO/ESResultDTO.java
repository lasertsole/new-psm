package com.psm.types.common.ES.DTO;

import com.psm.types.common.DTO.DTO;
import com.psm.types.common.ES.BO.ESResultBO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESResultDTO<T extends DTO> implements Serializable, DTO {
    private T document;
    private Map<String, List<String>> highlight;

    public static List<ESResultDTO> fromDOList(List<ESResultBO> list) {
        return list.stream().map(ESResultBO::toDTO).toList();
    }
}
