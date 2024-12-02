package com.psm.domain.User.relationships.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.psm.domain.User.relationships.types.convertor.RelationshipsConvertor;
import com.psm.types.common.BO.BO;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipsBO implements Serializable, BO<RelationshipsDTO> {

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long tgtUserId;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long srcUserId;

    private Boolean isFollowing;
    private Boolean isInContacts;
    private Boolean isBlocking;

    private String createTime;

    public static RelationshipsBO fromDTO(RelationshipsDTO relationshipsDTO) {
        return RelationshipsConvertor.INSTANCE.DTO2BO(relationshipsDTO);
    }

    public static RelationshipsBO fromDO(RelationshipsDO relationshipsDO) {
        return RelationshipsConvertor.INSTANCE.DO2BO(relationshipsDO);
    }

    @Override
    public RelationshipsDTO toDTO() {
        return RelationshipsConvertor.INSTANCE.BO2DTO(this);
    }
}
