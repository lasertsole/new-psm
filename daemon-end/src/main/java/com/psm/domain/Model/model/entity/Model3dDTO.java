package com.psm.domain.Model.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model3dDTO implements Serializable {
    private String id;
    private String userId;
    private String title;
    private String content;
    private MultipartFile coverFile;
    private String cover;
    private String entity;
    private String style;
    private String type;
    private Integer visible;
    private String storage;
    private String createTime;
}
