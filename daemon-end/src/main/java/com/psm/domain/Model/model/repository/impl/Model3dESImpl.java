package com.psm.domain.Model.model.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Model.model.repository.Model3dES;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class Model3dESImpl implements Model3dES {
    @Autowired
    private ElasticsearchClient elasticsearchClient;

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
}
