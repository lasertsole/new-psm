package com.psm.utils;

import com.alibaba.fastjson2.JSONObject;
import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import java.util.List;

public class CustomDeserialization implements DebeziumDeserializationSchema<String> {
    @Override
    public void deserialize(SourceRecord sourceRecord, Collector<String> collector) throws Exception {
        //1.创建一个JSONObject用来存放最终封装好的数据
        JSONObject result = new JSONObject();

        //2.获取数据库以及表名
        String topic = sourceRecord.topic();
        String[] split = topic.split("\\.");

        //数据库名
        String schema = split[1];
        //表名
        String tableName = split[2];


        //4.获取数据
        Struct value = (Struct) sourceRecord.value();

        //5.获取before数据
        Struct structBefore = value.getStruct("before");
        JSONObject beforeJson = new JSONObject();
        if (structBefore != null) {
            Schema schemas = structBefore.schema();
            List<Field> fields = schemas.fields();
            for (Field field : fields) {
                beforeJson.put(field.name(), structBefore.get(field));
            }
        }

        //6.获取after数据
        Struct structAfter = value.getStruct("after");
        JSONObject afterJson = new JSONObject();
        if (structAfter != null) {
            Schema schemas = structAfter.schema();
            List<Field> fields = schemas.fields();
            for (Field field : fields) {
                afterJson.put(field.name(), structAfter.get(field));
            }
        }

        String type="update";
        if(structBefore==null){
            type="insert";
        }
        if(structAfter==null){
            type="delete";
        }

        //将数据封装到JSONObject中
        result.put("schema", schema);
        result.put("tableName", tableName);
        result.put("before", beforeJson);
        result.put("after", afterJson);
        result.put("type", type);

        //将数据发送至下游
        collector.collect(result.toJSONString());
    }

    @Override
    public TypeInformation<String> getProducedType() {
        return BasicTypeInfo.STRING_TYPE_INFO;
    }
}

