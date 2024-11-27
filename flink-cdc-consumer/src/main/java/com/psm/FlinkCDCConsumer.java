package com.psm;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class FlinkCDCConsumer {
	public static void main(String[] args) throws Exception {
		// 配置 Kafka 消费者属性
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "default-group");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		// 创建 Kafka 消费者
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

		// 订阅主题
		consumer.subscribe(Collections.singletonList("ESCDC"));

		try {
			while (true) {
				// 拉取数据
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, String> record : records) {
					// 处理数据
					System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
				}
			}
		} finally {
			// 关闭消费者
			consumer.close();
		}
	}
}

