package com.psm.domain.Independent.User.Single.relationships.pojo.entity;

import com.psm.domain.Independent.User.Single.relationships.types.convertor.RelationshipsConvertor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.types.common.POJO.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipsDTO implements Serializable, DTO<RelationshipsBO> {
    private String id;
    private String tgtUserId;
    private String srcUserId;
    private Boolean isFollowing;
    private Boolean isInContacts;
    private Boolean isBlocking;

    public static RelationshipsDTO fromBO(RelationshipsBO relationshipsBO) {
        return RelationshipsConvertor.INSTANCE.BO2DTO(relationshipsBO);
    }

    @Override
    public RelationshipsBO toBO() {
        return RelationshipsConvertor.INSTANCE.DTO2BO(this);
    }
}
