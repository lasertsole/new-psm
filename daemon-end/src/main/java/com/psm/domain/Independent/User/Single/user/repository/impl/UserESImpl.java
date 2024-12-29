package com.psm.domain.Independent.User.Single.user.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.User.Single.user.repository.UserES;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class UserESImpl implements UserES {
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @PostConstruct
    public void startup() throws Exception {
        boolean indexExists = elasticsearchClient.indices().exists(i -> i.index("tb_users")).value();
        if (indexExists) return;

        elasticsearchClient.indices().create(create -> create
            .index("tb_users")
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
                .properties("name", p -> p.text(t -> t.analyzer("ik_max_word")))
                .properties("profile", p -> p.text(t -> t.analyzer("ik_smart")))
            )
        ).acknowledged();
    }
}
