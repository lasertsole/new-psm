package com.psm.infrastructure.ES;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ESApi {
    @Autowired
    private ElasticsearchClient esClient;

    public void searchESHighLightData(String indexName) throws IOException {
        try {
            // 创建 SearchRequest
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("tb_3d_models")
                    .query(q -> q
                            .match(m -> m
                                    .field("title") // 指定要查询的字段
                                    .query("test") // 指定查询的关键字
                            )
                    )
                    .highlight(h -> h
                            .fields("title", f -> f
                                    .fragmentSize(150) // 高亮片段的大小
                                    .numberOfFragments(3) // 高亮片段的数量
                            )
                    )
            );

            // 执行搜索
            SearchResponse<JSONObject> searchResponse = esClient.search(searchRequest, JSONObject.class);

            // 处理搜索结果
            List<Hit<JSONObject>> hits = searchResponse.hits().hits();
            for (Hit<JSONObject> hit : hits) {
                JSONObject source = hit.source();
                Map<String, List<String>> highlightFields = hit.highlight();

                if (highlightFields != null && highlightFields.containsKey("title")) {
                    List<String> titleHighlight = highlightFields.get("title");
                    System.out.println("Document found: " + source.toJSONString());
                    System.out.println("Highlighted title: " + titleHighlight);
                } else {
                    System.out.println("Document found: " + source.toJSONString());
                }
            }

        } catch (Exception e) {
            throw e;
        }
    }
}
