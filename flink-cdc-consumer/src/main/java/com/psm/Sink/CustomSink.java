package com.psm.Sink;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.core.io.ClassPathResource;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomSink extends RichSinkFunction {
    RestHighLevelClient highLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

    private ClassPathResource template_res = new ClassPathResource("es/template.json");
    @Override
    public void invoke(Object value, Context context) throws Exception {
        JSONObject jsonObject = JSON.parseObject(value.toString());
        JSONObject jsonAfter = JSON.parseObject(jsonObject.get("after").toString());
        log.info("jsonObject:{}", jsonObject);
        log.info("CustomSink:{}", jsonAfter);
    }

//    /**
//     * 创建索引模板
//     *
//     * @param indexname
//     */
//    public void CreateMapping(String indexname) {
//
//        CreateIndexRequest indexRequest = new CreateIndexRequest(indexname);
//        try {
//
//            if (checkMapping(indexname)) {
//                return;
//            }
//            String jsontemplate = readJsonFile(template_res);
//            JSONObject templateObj = JSON.parseObject(jsontemplate);
//            // 添加索引的mapping规则
//            indexRequest.mapping("_doc", templateObj.getJSONObject("mappings").toString(), XContentType.JSON);
//
//            // 添加索引的settings规则
//            indexRequest.settings(templateObj.getJSONObject("settings").toString(), XContentType.JSON);
//
//            // 添加索引的aliases规则
//            indexRequest.aliases(templateObj.getJSONObject("aliases").getInnerMap());
//
//            // 发送请求
//            CreateIndexResponse createIndexResponse = highLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
//            System.out.println(createIndexResponse);
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//
//
//    }
//
//    private boolean checkMapping(String indexname) {
//        try {
//            //构建请求
//            GetMappingsRequest request = new GetMappingsRequest().indices(indexname);
//
//            GetMappingsResponse response = highLevelClient.indices().getMapping(request, RequestOptions.DEFAULT);
//
//            if (response.mappings() != null) {
//                return true;
//            }
//        } catch (Exception ex) {
//
//        }
//        return false;
//    }
//
//    public void addElasticsearchData(String indexname, List<Map<String, Object>> addEsDataMapList) {
//        try {
//            //创建请求
//            BulkRequest bulkRequest = new BulkRequest();
//            //创建index请求 千万注意，这个写在循环外侧，否则UDP协议会有丢数据的情况。
//            IndexRequest requestData = null;
//            for (Map<String, Object> addEsDataMap : addEsDataMapList) {//添加数据
//                requestData = new IndexRequest(indexname).type("_doc").id(addEsDataMap.get("num_id").toString()).source(addEsDataMap);
//                bulkRequest.add(requestData);
//            }
//            log.info("es同步数据数量:{}", bulkRequest.numberOfActions());
//            //设置索引刷新规则
//            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
//            //分批次提交，数量控制
//            if (bulkRequest.numberOfActions() >= 1) {
//                BulkResponse bulkResponse = highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//                if (!bulkResponse.hasFailures()) {
//                    log.info("es同步数据结果:{}", bulkResponse.buildFailureMessage());
//                    System.out.println("es同步数据结果:" + bulkRequest.numberOfActions());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("es同步数据执行失败:{}", addEsDataMapList);
//        } finally {
//
//        }
//    }
//
//    public String readJsonFile(Resource resource) throws Exception {
//        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
//        StringBuffer message = new StringBuffer();
//        String line = null;
//        while ((line = br.readLine()) != null) {
//            message.append(line);
//        }
//        return message.toString();
//    }
}
