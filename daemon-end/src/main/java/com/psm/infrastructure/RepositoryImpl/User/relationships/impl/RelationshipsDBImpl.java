package com.psm.infrastructure.RepositoryImpl.User.relationships.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.psm.domain.Independent.User.Single.relationships.entity.RelationshipsDO;
import com.psm.infrastructure.DB.RelationshipsMapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import com.psm.infrastructure.RepositoryImpl.User.relationships.RelationshipsDB;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Repository
public class RelationshipsDBImpl extends BaseDBRepositoryImpl<RelationshipsMapper, RelationshipsDO> implements RelationshipsDB {
    @Autowired
    private RelationshipsMapper relationshipsMapper;

    @Override
    public List<RelationshipsDO> selectByTgtUserId(RelationshipsDO relationshipsDO) {
        LambdaQueryWrapper<RelationshipsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationshipsDO::getTgtUserId,relationshipsDO.getTgtUserId());
        wrapper.eq(Objects.nonNull(relationshipsDO.getIsFollowing()), RelationshipsDO::getIsFollowing, relationshipsDO.getIsFollowing());
        wrapper.eq(Objects.nonNull(relationshipsDO.getIsInContacts()), RelationshipsDO::getIsInContacts, relationshipsDO.getIsInContacts());
        wrapper.eq(Objects.nonNull(relationshipsDO.getIsBlocking()), RelationshipsDO::getIsBlocking, relationshipsDO.getIsBlocking());

        return relationshipsMapper.selectList(wrapper);
    }

    @Override
    public List<RelationshipsDO> selectBySrcUserId(RelationshipsDO relationshipsDO) {
        LambdaQueryWrapper<RelationshipsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationshipsDO::getSrcUserId,relationshipsDO.getSrcUserId());
        wrapper.eq(Objects.nonNull(relationshipsDO.getIsFollowing()), RelationshipsDO::getIsFollowing, relationshipsDO.getIsFollowing());
        wrapper.eq(Objects.nonNull(relationshipsDO.getIsInContacts()), RelationshipsDO::getIsInContacts, relationshipsDO.getIsInContacts());
        wrapper.eq(Objects.nonNull(relationshipsDO.getIsBlocking()), RelationshipsDO::getIsBlocking, relationshipsDO.getIsBlocking());

        return relationshipsMapper.selectList(wrapper);
    }

    @Override
    public RelationshipsDO selectByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) {
        LambdaQueryWrapper<RelationshipsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationshipsDO::getTgtUserId,tgtUserId).and(
            w->w.eq(RelationshipsDO::getSrcUserId,srcUserId));

        return relationshipsMapper.selectOne(wrapper);
    }

    @Override
    public void deleteByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) {
        LambdaQueryWrapper<RelationshipsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationshipsDO::getTgtUserId,tgtUserId).and(
            w->w.eq(RelationshipsDO::getSrcUserId,srcUserId));

        relationshipsMapper.delete(wrapper);
    }

    @Override
    public void insertOrUpdateRelationship(RelationshipsDO relationshipsDO) {
        LambdaUpdateWrapper<RelationshipsDO> wrapper = new LambdaUpdateWrapper<>();

        // 根据tgtUserId和srcUserId匹配的更新
        wrapper.eq(RelationshipsDO::getTgtUserId,relationshipsDO.getTgtUserId())
                    .and(aw->aw.eq(RelationshipsDO::getSrcUserId,relationshipsDO.getSrcUserId()));

        // 如果关系属性除了id、tgtUserId、srcUserId以外不为空且全为false，则删除该条记录，节省存储空间。否则更新属性
        if(
            Objects.nonNull(relationshipsDO.getIsFollowing())&&!relationshipsDO.getIsFollowing()
            && Objects.nonNull(relationshipsDO.getIsInContacts())&&!relationshipsDO.getIsInContacts()
            && Objects.nonNull(relationshipsDO.getIsBlocking())&&!relationshipsDO.getIsBlocking()
        ) {
            relationshipsMapper.delete(wrapper);
            return;
        }

        // 更新关注属性
        saveOrUpdate(relationshipsDO, wrapper);
    }

    @Override
    public RelationshipsDO selectRelationship(RelationshipsDO relationshipsDO) {
        LambdaQueryWrapper<RelationshipsDO> wrapper = new LambdaQueryWrapper<>();

        // 根据tgtUserId和srcUserId匹配的更新
        wrapper.eq(RelationshipsDO::getTgtUserId,relationshipsDO.getTgtUserId())
                    .and(aw->aw.eq(RelationshipsDO::getSrcUserId,relationshipsDO.getSrcUserId()));

        wrapper.eq(Objects.nonNull(relationshipsDO.getIsFollowing()), RelationshipsDO::getIsFollowing, relationshipsDO.getIsFollowing());
        wrapper.eq(Objects.nonNull(relationshipsDO.getIsInContacts()), RelationshipsDO::getIsInContacts, relationshipsDO.getIsInContacts());
        wrapper.eq(Objects.nonNull(relationshipsDO.getIsBlocking()), RelationshipsDO::getIsBlocking, relationshipsDO.getIsBlocking());

        RelationshipsDO result = relationshipsMapper.selectOne(wrapper);

        // 如果关系属性除了id、tgtUserId、srcUserId以外不为空且全为false，则删除该条记录，节省存储空间。否则更新属性
        if(
            Objects.nonNull(result.getIsFollowing())&&!result.getIsFollowing()
            && Objects.nonNull(result.getIsInContacts())&&!result.getIsInContacts()
            && Objects.nonNull(relationshipsDO.getIsBlocking())&&!relationshipsDO.getIsBlocking()
        ) {
            relationshipsMapper.delete(wrapper);
        }

        return result;
    }
}
