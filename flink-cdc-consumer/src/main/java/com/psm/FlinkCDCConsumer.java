package com.psm;

import com.psm.Sink.CustomSink;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

@Slf4j
public class FlinkCDCConsumer {
	public static void main(String[] args) throws Exception {
		// 创建 Kafka 生产者
		Properties kafkaProps = new Properties();
		kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "default-group");
		kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		kafkaProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaProps.setProperty("topic", "ESCDC");

		FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(
				kafkaProps.getProperty("topic"),
				new SimpleStringSchema(),
				kafkaProps
		);

		// 设置 Watermark 策略（可选）
		kafkaConsumer.setStartFromEarliest(); // 从最早的偏移量开始消费

		// 创建 Flink 执行环境
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// 添加 Kafka 源
		DataStream<String> stream = env.addSource(kafkaConsumer);
		stream.addSink(new CustomSink());

		// 启动 Flink 任务
		env.execute("flinkPgCDCConsumer");
	}
}

