package com.psm.domain.Independent.User.Single.relationships.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.psm.app.annotation.validation.MustNull;
import com.psm.domain.Independent.User.Single.relationships.types.convertor.RelationshipsConvertor;
import com.psm.types.common.POJO.BO;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipsBO implements Serializable, BO<RelationshipsDTO, RelationshipsDO> {

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long tgtUserId;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long srcUserId;

    private Boolean isFollowing;
    private Boolean isInContacts;
    private Boolean isBlocking;

    @MustNull
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

    @Override
    public RelationshipsDO toDO() {
        return RelationshipsConvertor.INSTANCE.BO2DO(this);
    }
}
