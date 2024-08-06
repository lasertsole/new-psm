package com.psm.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    @Size(min = 1, max = 50, message = "页码必须在1-50之间")
    private Integer currentPage;

    @Size(min = 1, max = 50, message = "每页数量必须在1-50之间")
    private Integer pageSize;
}
