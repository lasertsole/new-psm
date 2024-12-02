package com.psm.infrastructure.ES;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
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
        }
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
}
