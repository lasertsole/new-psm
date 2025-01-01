package com.psm.infrastructure.RepositoryImpl.User.relationships.impl;

import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.User.Single.relationships.pojo.entity.RelationshipsDO;
import com.psm.domain.Independent.User.Single.relationships.repository.RelationshipsRepository;
import com.psm.infrastructure.RepositoryImpl.User.relationships.RelationshipsDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Repository
public class RelationshipsRepositoryImpl implements RelationshipsRepository {
    @Autowired
    private RelationshipsDB relationshipsDB;

    @Override
    public List<RelationshipsDO> DBSelectByTgtUserId(RelationshipsDO relationshipsDO) {
        return relationshipsDB.selectByTgtUserId(relationshipsDO);
    }

    @Override
    public List<RelationshipsDO> DBSelectBySrcUserId(RelationshipsDO relationshipsDO) {
        return relationshipsDB.selectBySrcUserId(relationshipsDO);
    }

    @Override
    public RelationshipsDO DBSelectRelationship(RelationshipsDO relationshipsDO) {
        return relationshipsDB.selectRelationship(relationshipsDO);
    }

    @Override
    public void DBInsertOrUpdateRelationship(RelationshipsDO relationshipsDO) {
        relationshipsDB.insertOrUpdateRelationship(relationshipsDO);
    }
}
