package com.psm.infrastructure.SocketIO.POJOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Room implements Serializable {
    private String roomId;
    private String roomOwnerId;
    private String roomName;
    private String roomType;
    private Set<String> memberIdSet;
}
