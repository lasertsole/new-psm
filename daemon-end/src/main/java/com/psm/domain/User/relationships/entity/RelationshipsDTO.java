package com.psm.domain.User.relationships.entity;

import com.psm.domain.User.relationships.types.convertor.RelationshipsConvertor;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipsDTO implements Serializable {
    private String id;
    private String tgtUserId;
    private String srcUserId;
    private Boolean isFollowing;
    private Boolean isInContacts;
    private Boolean isBlocking;

    public static RelationshipsDTO fromBO(RelationshipsBO relationshipsBO) {
        return RelationshipsConvertor.INSTANCE.BO2DTO(relationshipsBO);
    }
}
