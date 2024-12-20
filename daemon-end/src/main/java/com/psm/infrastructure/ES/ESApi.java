package com.psm.infrastructure.ES;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOptionsBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ESApi {
    @Autowired
    private ElasticsearchClient esClient;

    /**
     * 将List<Object>转换为List<FieldValue>
     *
     * @param values 待转换的List
     * @return List<FieldValue>
     */
    private List<FieldValue> convertToFieldValues(List<Object> values) {
        List<FieldValue> fieldValues = new ArrayList<>();

        for (Object value : values) {
            if (value instanceof Long) {
                fieldValues.add(FieldValue.of((Long) value));
            } else if (value instanceof Double) {
                fieldValues.add(FieldValue.of((Double) value));
            } else if (value instanceof Boolean) {
                fieldValues.add(FieldValue.of((Boolean) value));
            } else if (value instanceof String) {
                fieldValues.add(FieldValue.of((String) value));
            } else if (value == null) {
                fieldValues.add(FieldValue.NULL);
            } else {
                // 如果遇到未知类型，可以选择抛出异常或使用默认值
                fieldValues.add(FieldValue.of(value)); // 使用 JsonData 包装
            }
        }

        return fieldValues;
    }

    /**
     * 基于from size的普通查询
     *
     * @param indexName es索引名称
     * @param params 查询参数
     * @param from 查询起始位置
     * @param size 查询结果数量
     * @return 查询结果
     * @throws IOException IO异常
     */
    public Map<String, Object> searchESHighLightData(String indexName, Map<String, Object> params, Integer from, Integer size) throws IOException {
        // 创建 BoolQuery.Builder
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        // 创建 Highlight.Builder
        Highlight.Builder highlightBuilder = new Highlight.Builder();

        // 遍历 params 并添加 match 查询
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            Query query;
            if (value instanceof String) {
                query = MatchQuery.of(m -> m.field(field).query((String) value))._toQuery();
            } else {
                query = TermQuery.of(t -> t.field(field).value(v -> v.longValue((Long) value)))._toQuery();
            }
            boolQueryBuilder.should(query);
            highlightBuilder.fields(field, f -> f
                    .fragmentSize(150) // 高亮片段的大小
                    .numberOfFragments(3) // 高亮片段的数量
                    .preTags("<mark>")
                    .postTags("</mark>")
            );
        };

        try {
            // 创建 SearchRequest
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(Query.of(q -> q.bool(boolQueryBuilder.build())))
                    .highlight(highlightBuilder.build())
                    .from(from)
                    .size(size)
            );

            // 执行搜索
            SearchResponse<JSONObject> searchResponse = esClient.search(searchRequest, JSONObject.class);

            // 处理搜索结果
            List<Hit<JSONObject>> hits = searchResponse.hits().hits();
            Map<String, Object> results = new HashMap<>();
            List<Map<String, Object>> records = new ArrayList<>();
            for (Hit<JSONObject> hit : hits) {
                Map<String, Object> result = Map.of( "document", hit.source(), "highlight", hit.highlight());
                records.add(result);
            };
            results.put("records", records);
            results.put("total", searchResponse.hits().total().value());
            results.put("size", size);
            results.put("from", from);

            return results;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 基于search after的深分页查询
     *
     * @param indexName es索引名称
     * @param params 查询参数
     * @param afterKeys searchAfter列表
     * @param size 查询结果数量
     * @return 查询结果
     * @throws IOException IO异常
     */
    public Map<String, Object> searchESHighLightData(String indexName, Map<String, Object> params, List<String> orderKeys, List<Object> afterKeys, Integer size) throws IOException {
        if(orderKeys == null || orderKeys.isEmpty()) {
            throw new IllegalArgumentException("orderKeys cannot be empty");
        } else if( afterKeys!= null && orderKeys.size() != afterKeys.size()) {
            throw new IllegalArgumentException("orderKeys and afterKeys must have the same size");
        };

        // 创建 BoolQuery.Builder
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        // 创建 Highlight.Builder
        Highlight.Builder highlightBuilder = new Highlight.Builder();
        // 创建 SortOptions.Builder
        SortOptions.Builder sortOptionsBuilder = new SortOptions.Builder();

        // 遍历 params 并添加 match 查询
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            Query query;
            if (value instanceof String) {
                query = MatchQuery.of(m -> m.field(field).query((String) value))._toQuery();
            } else {
                query = TermQuery.of(t -> t.field(field).value(v -> v.longValue((Long) value)))._toQuery();
            }
            boolQueryBuilder.should(query);
            highlightBuilder.fields(field, f -> f
                    .fragmentSize(150) // 高亮片段的大小
                    .numberOfFragments(3) // 高亮片段的数量
                    .preTags("<mark>")
                    .postTags("</mark>")
            );
        };

        // 创造排序条件
        for(String orderKey :orderKeys) {
            sortOptionsBuilder.field(f -> f.field(orderKey).order(SortOrder.Desc));
        };

        try {
            // 创建 SearchRequest
            SearchRequest searchRequest = SearchRequest.of(s -> {s
                    .index(indexName)
                    .query(Query.of(q -> q.bool(boolQueryBuilder.build())))
                    .highlight(highlightBuilder.build())
                    .size(size)
                    .sort(sortOptionsBuilder.build());
                    if (afterKeys != null) {s.searchAfter(convertToFieldValues(afterKeys));};

                    return s;
                }
            );

            // 执行搜索
            SearchResponse<JSONObject> searchResponse = esClient.search(searchRequest, JSONObject.class);

            // 处理搜索结果
            List<Hit<JSONObject>> hits = searchResponse.hits().hits();
            Map<String, Object> results = new HashMap<>();
            List<Map<String, Object>> records = new ArrayList<>();
            for (Hit<JSONObject> hit : hits) {
                Map<String, Object> result = Map.of( "document", hit.source(), "highlight", hit.highlight());
                records.add(result);
            };
            results.put("records", records);
            results.put("total", searchResponse.hits().total().value());
            results.put("size", size);

            // 获取下一个 afterKey
            if (!hits.isEmpty() && hits.size() != size) {
                List<Object> nextAfterKeys = new ArrayList<>();
                orderKeys.forEach( key -> nextAfterKeys.add(hits.get(hits.size() - 1).source().get(key)));
                results.put("nextAfterKeys", nextAfterKeys);
            } else {
                results.put("nextAfterKeys", null);
            };

            return results;
        } catch (Exception e) {
            throw e;
        }
    }
}
