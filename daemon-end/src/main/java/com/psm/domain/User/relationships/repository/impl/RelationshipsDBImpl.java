package com.psm.domain.User.relationships.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.psm.domain.User.relationships.entity.RelationshipsDAO;
import com.psm.domain.User.relationships.repository.RelationshipsDB;
import com.psm.infrastructure.DB.RelationshipsMapper;
import com.psm.app.annotation.spring.Repository;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Repository
public class RelationshipsDBImpl extends BaseDBRepositoryImpl<RelationshipsMapper, RelationshipsDAO> implements RelationshipsDB {
    @Autowired
    private RelationshipsMapper relationshipsMapper;

    @Override
    public List<RelationshipsDAO> selectByTgtUserId(RelationshipsDAO relationshipsDAO) {
        LambdaQueryWrapper<RelationshipsDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationshipsDAO::getTgtUserId,relationshipsDAO.getTgtUserId());
        wrapper.eq(Objects.nonNull(relationshipsDAO.getIsFollowing()), RelationshipsDAO::getIsFollowing, relationshipsDAO.getIsFollowing());
        wrapper.eq(Objects.nonNull(relationshipsDAO.getIsInContacts()), RelationshipsDAO::getIsInContacts, relationshipsDAO.getIsInContacts());
        wrapper.eq(Objects.nonNull(relationshipsDAO.getIsBlocking()), RelationshipsDAO::getIsBlocking, relationshipsDAO.getIsBlocking());

        return relationshipsMapper.selectList(wrapper);
    }

    @Override
    public List<RelationshipsDAO> selectBySrcUserId(RelationshipsDAO relationshipsDAO) {
        LambdaQueryWrapper<RelationshipsDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationshipsDAO::getSrcUserId,relationshipsDAO.getSrcUserId());
        wrapper.eq(Objects.nonNull(relationshipsDAO.getIsFollowing()), RelationshipsDAO::getIsFollowing, relationshipsDAO.getIsFollowing());
        wrapper.eq(Objects.nonNull(relationshipsDAO.getIsInContacts()), RelationshipsDAO::getIsInContacts, relationshipsDAO.getIsInContacts());
        wrapper.eq(Objects.nonNull(relationshipsDAO.getIsBlocking()), RelationshipsDAO::getIsBlocking, relationshipsDAO.getIsBlocking());

        return relationshipsMapper.selectList(wrapper);
    }

    @Override
    public RelationshipsDAO selectByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) {
        LambdaQueryWrapper<RelationshipsDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationshipsDAO::getTgtUserId,tgtUserId).and(
            w->w.eq(RelationshipsDAO::getSrcUserId,srcUserId));

        return relationshipsMapper.selectOne(wrapper);
    }

    @Override
    public void deleteByTgUserIdAndSrcUserId(Long tgtUserId, Long srcUserId) {
        LambdaQueryWrapper<RelationshipsDAO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RelationshipsDAO::getTgtUserId,tgtUserId).and(
            w->w.eq(RelationshipsDAO::getSrcUserId,srcUserId));

        relationshipsMapper.delete(wrapper);
    }

    @Override
    public void insertOrUpdateRelationship(RelationshipsDAO relationshipsDAO) {
        LambdaUpdateWrapper<RelationshipsDAO> wrapper = new LambdaUpdateWrapper<>();

        // 根据tgtUserId和srcUserId匹配的更新
        wrapper.eq(RelationshipsDAO::getTgtUserId,relationshipsDAO.getTgtUserId())
                    .and(aw->aw.eq(RelationshipsDAO::getSrcUserId,relationshipsDAO.getSrcUserId()));

        // 如果关系属性除了id、tgtUserId、srcUserId以外不为空且全为false，则删除该条记录，节省存储空间。否则更新属性
        if(
            Objects.nonNull(relationshipsDAO.getIsFollowing())&&!relationshipsDAO.getIsFollowing()
            && Objects.nonNull(relationshipsDAO.getIsInContacts())&&!relationshipsDAO.getIsInContacts()
            && Objects.nonNull(relationshipsDAO.getIsBlocking())&&!relationshipsDAO.getIsBlocking()
        ) {
            relationshipsMapper.delete(wrapper);
            return;
        }

        // 更新关注属性
        saveOrUpdate(relationshipsDAO, wrapper);
    }

    @Override
    public RelationshipsDAO selectRelationship(RelationshipsDAO relationshipsDAO) {
        LambdaQueryWrapper<RelationshipsDAO> wrapper = new LambdaQueryWrapper<>();

        // 根据tgtUserId和srcUserId匹配的更新
        wrapper.eq(RelationshipsDAO::getTgtUserId,relationshipsDAO.getTgtUserId())
                    .and(aw->aw.eq(RelationshipsDAO::getSrcUserId,relationshipsDAO.getSrcUserId()));

        wrapper.eq(Objects.nonNull(relationshipsDAO.getIsFollowing()), RelationshipsDAO::getIsFollowing, relationshipsDAO.getIsFollowing());
        wrapper.eq(Objects.nonNull(relationshipsDAO.getIsInContacts()), RelationshipsDAO::getIsInContacts, relationshipsDAO.getIsInContacts());
        wrapper.eq(Objects.nonNull(relationshipsDAO.getIsBlocking()), RelationshipsDAO::getIsBlocking, relationshipsDAO.getIsBlocking());

        RelationshipsDAO result = relationshipsMapper.selectOne(wrapper);

        // 如果关系属性除了id、tgtUserId、srcUserId以外不为空且全为false，则删除该条记录，节省存储空间。否则更新属性
        if(
            Objects.nonNull(result.getIsFollowing())&&!result.getIsFollowing()
            && Objects.nonNull(result.getIsInContacts())&&!result.getIsInContacts()
            && Objects.nonNull(relationshipsDAO.getIsBlocking())&&!relationshipsDAO.getIsBlocking()
        ) {
            relationshipsMapper.delete(wrapper);
        }

        return result;
    }
}
