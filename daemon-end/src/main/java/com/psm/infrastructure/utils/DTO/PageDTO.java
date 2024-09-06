package com.psm.infrastructure.utils.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    @Min(value = 1, message = "页码必须大于1")
    @Max(value = 50, message = "页码必须小于50")
    private Integer currentPage = 1;//当前页码(默认值为1)

    @Min(value = 1, message = "每页项数必须大于1")
    @Max(value = 50, message = "每页项数必须小于50")
    private Integer pageSize = 10;//每页项数(默认值为10)
}
