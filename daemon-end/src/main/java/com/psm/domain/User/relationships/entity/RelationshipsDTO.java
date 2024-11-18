package com.psm.domain.User.relationships.entity;

import com.psm.domain.User.relationships.types.convertor.RelationshipsConvertor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.utils.VO.DTO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipsDTO implements Serializable, DTO2VOable<RelationshipsVO> {
    private Long id;
    private Long tgtUserId;
    private Long srcUserId;
    private Boolean isFollowing;
    private Boolean isInContacts;
    private Boolean isBlocking;

    public static RelationshipsDTO fromBO(RelationshipsBO relationshipsBO) {
        return RelationshipsConvertor.INSTANCE.BO2DTO(relationshipsBO);
    }

    @Override
    public RelationshipsVO toVO() {
        return RelationshipsConvertor.INSTANCE.DTO2VO(this);
    }
}
