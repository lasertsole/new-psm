package com.psm.types.common.ES.DO;

import com.psm.types.common.BO.BO;
import com.psm.types.common.DO.DO;
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
public class ESResultDO<T extends DO> implements Serializable, DO<ESResultBO> {
    private T document;
    private Map<String, List<String>> highlight;

    @Override
    public ESResultBO toBO() {
        return new ESResultBO((BO) document.toBO(), highlight);
    }
}
