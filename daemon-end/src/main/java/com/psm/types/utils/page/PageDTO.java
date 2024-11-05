package com.psm.types.utils.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4076807066395539372L;

    @Min(value = 1, message = "页码必须大于1")
    @Max(value = 50, message = "页码必须小于50")
    private Integer current = 1;//当前页码(默认值为1)

    @Min(value = 1, message = "每页项数必须大于1")
    @Max(value = 50, message = "每页项数必须小于50")
    private Integer size = 10;//每页项数(默认值为10)
}
