package com.psm.domain.User.relationships.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipsDTO implements Serializable {

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long tgtUserId;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long srcUserId;

    private Boolean isFollowing;

    private Boolean isInContacts;

    private Boolean isBlocking;
}
