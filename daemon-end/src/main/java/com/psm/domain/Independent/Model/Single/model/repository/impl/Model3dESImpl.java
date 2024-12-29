package com.psm.domain.Independent.Model.Single.model.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.alibaba.fastjson2.JSONObject;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.Model.Single.model.entity.Model3dDTO;
import com.psm.domain.Independent.Model.Single.model.repository.Model3dES;
import com.psm.infrastructure.ES.ESApi;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class Model3dESImpl implements Model3dES {
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private ESApi esApi;

    @PostConstruct
    public void startup() throws Exception {
        boolean indexExists = elasticsearchClient.indices().exists(i -> i.index("tb_3d_models")).value();
        if (indexExists) return;

        elasticsearchClient.indices().create(create -> create
            .index("tb_3d_models")
            .settings(settings -> settings
                .analysis(analysis -> analysis
                    .analyzer("ik_smart", analyzer -> analyzer
                        .custom(custom -> custom
                            .tokenizer("ik_smart")
                            .charFilter("html_strip")
                            .filter("lowercase", "asciifolding")
                        )
                    )
                    .analyzer("ik_max_word", analyzer -> analyzer
                        .custom(custom -> custom
                            .tokenizer("ik_max_word")
                            .charFilter("html_strip")
                            .filter("lowercase", "asciifolding")
                        )
                    )
                )
            )
            .mappings(mappings -> mappings
                .properties("id", p -> p.long_(l -> l))
                .properties("userId", p -> p.long_(l -> l))
                .properties("title", p -> p.text(t -> t.analyzer("ik_smart")))
                .properties("content", p -> p.text(t -> t.analyzer("ik_smart")))
            )
        ).acknowledged();
    }

    private boolean canConvertToLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> selectBlurSearchModel3d(String keyword) throws IOException {
        Map<String, Object> params = new HashMap<>();
        if (canConvertToLong(keyword)) {
            params.put("id", Long.parseLong(keyword));
            params.put("userId", Long.parseLong(keyword));
        };
        params.put("title", keyword);
        params.put("content", keyword);

        Map<String, Object> map = esApi.searchESHighLightData("tb_3d_models", params, 0, 10);
        List<Map<String, Object>> records = (List<Map<String, Object>>) map.get("records");

        return records.stream().map(m -> {
            JSONObject document = (JSONObject) m.get("document");
            Model3dDTO model3dDTO = JSONObject.parseObject(document.toJSONString(), Model3dDTO.class);
            return Map.of("document", model3dDTO, "highlight", m.get("highlight"));
        }).toList();
    }

    @Override
    public Map<String, Object> selectDetailSearchModel3d(String keyword, Long afterKeyId, Integer size) throws IOException {
        Map<String, Object> params = new HashMap<>();
        if (canConvertToLong(keyword)) {
            params.put("id", Long.parseLong(keyword));
            params.put("userId", Long.parseLong(keyword));
        };
        params.put("title", keyword);
        params.put("content", keyword);

        List<String> orderKeys = List.of("id");
        List<Object> afterKeys = Optional.ofNullable(afterKeyId).map(id -> List.of((Object) id)).orElse(null);

        Map<String, Object> map = esApi.searchESHighLightData("tb_3d_models", params, orderKeys, afterKeys, size);
        List<Map<String, Object>> records = (List<Map<String, Object>>) map.get("records");
        List<Map<String, Object>> transformList = records.stream().map(m -> {
            JSONObject document = (JSONObject) m.get("document");
            Model3dDTO model3dDTO = JSONObject.parseObject(document.toJSONString(), Model3dDTO.class);
            return Map.of("document", model3dDTO, "highlight", m.get("highlight"));
        }).toList();
        map.put("records", transformList);

        // 将nextAfterKeys内数值类型元素转换成字符串类型元素
        map.put("nextAfterKeys",
                Optional.ofNullable(map.get("nextAfterKeys"))
                        .map(nextAfterKeys -> ((List<Object>) nextAfterKeys).stream().map(String::valueOf).toList()).orElse(null)
        );

        return map;
    }
}
