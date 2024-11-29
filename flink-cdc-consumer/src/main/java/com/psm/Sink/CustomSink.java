package com.psm.Sink;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.psm.Client.ESClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CustomSink extends RichSinkFunction<String[]> {
    /**
     * 批量处理数据参数
     */
    @Override
    public void invoke(String[] values, Context context) throws Exception {
        List<JSONObject> jsonList = Arrays.stream(values).map(JSON::parseObject).toList();

        List<JSONObject> esDataList = new ArrayList<>();
        List<BulkOperation> bulkOperations = new ArrayList<>();
        try {
            jsonList.forEach(json -> {
                JSONObject jsonBefore = JSON.parseObject(json.get("before").toString());
                JSONObject jsonAfter = JSON.parseObject(json.get("after").toString());
                String tableName = (String) json.get("tableName");
                String type = (String) json.get("type");

                if ("insert".equals(type) || "update".equals(type)) {
                    IndexOperation operation = IndexOperation.of(o -> o
                            .index(tableName)
                            .id(String.valueOf(jsonAfter.get("id")))
                            .document(jsonAfter)
                    );
                    bulkOperations.add(operation._toBulkOperation());
                } else if ("delete".equals(type)) {
                    DeleteOperation operation = DeleteOperation.of(o -> o
                            .index(tableName)
                            .id(String.valueOf(jsonAfter.get("id")))
                    );
                    bulkOperations.add(operation._toBulkOperation());
                }
            });

            int batchSize = 50; // 设置批量处理的大小
            List<List<BulkOperation>> partitions = IntStream.range(0, (bulkOperations.size() + batchSize - 1) / batchSize)
                    .mapToObj(i -> bulkOperations.subList(i * batchSize, Math.min((i + 1) * batchSize, bulkOperations.size())))
                    .collect(Collectors.toList());

            partitions.forEach(partition -> {
                BulkRequest bulkRequest = BulkRequest.of(b -> b.operations(partition));
                bulkRequest.refresh(); // 执行批量操作后,立即刷新索引，使这些更改立即可见
                BulkResponse bulk = null;
                try {
                    bulk = ESClient.getESClient().bulk(bulkRequest);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!bulk.errors()) {
                    log.info("Bulk response: " + bulk.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("es同步数据执行失败:{}", esDataList);
        }
    }
}
