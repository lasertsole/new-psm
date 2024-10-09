package com.psm.infrastructure.utils.Mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@Component
public class MongoUtil {

    @Autowired
    private MongoTemplate mongoTemplate;

    // 插入文档
    public <T> T insert(T object, String collectionName) {
        return mongoTemplate.insert(object, collectionName);
    }

    // 查询单个文档
    public <T> T findOne(Query query, Class<T> clazz, String collectionName) {
        return mongoTemplate.findOne(query, clazz, collectionName);
    }

    // 查询多个文档
    public <T> List<T> findList(Query query, Class<T> clazz, String collectionName) {
        return mongoTemplate.find(query, clazz, collectionName);
    }

    // 更新文档
    public Long update(Query query, Update update, Class<?> clazz, String collectionName) {
        return mongoTemplate.updateFirst(query, update, clazz, collectionName).getModifiedCount();
    }

    // 删除文档
    public Long delete(Query query, Class<?> clazz, String collectionName) {
        return mongoTemplate.remove(query, clazz, collectionName).getDeletedCount();
    }

    // 聚合查询
    public <T> AggregationResults<T> aggregate(Aggregation aggregation, String collectionName, Class<T> resultClass) {
        return mongoTemplate.aggregate(aggregation, collectionName, resultClass);
    }
}
